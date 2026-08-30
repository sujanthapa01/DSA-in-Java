import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class AudioBalancer extends JFrame {

    private final JSlider leftSlider;
    private final JSlider rightSlider;

    public AudioBalancer() {

        setTitle("Audio Balancer");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // -----------------------------
        // Main panel
        // -----------------------------

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("🎧 Audio Balancer");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(25));

        // -----------------------------
        // Left channel
        // -----------------------------

        JLabel leftLabel = new JLabel("Left: 100%");
        leftLabel.setFont(new Font("Arial", Font.BOLD, 16));

        leftSlider = new JSlider(0, 100, 100);
        leftSlider.setMajorTickSpacing(25);
        leftSlider.setMinorTickSpacing(5);
        leftSlider.setPaintTicks(true);
        leftSlider.setPaintLabels(true);

        leftSlider.addChangeListener(e -> {

            leftLabel.setText("Left: " + leftSlider.getValue() + "%");

            // Apply live once the user releases the slider (or uses
            // arrow keys, which fire with adjusting == false), instead
            // of requiring a click on "Apply" every time.
            if (!leftSlider.getValueIsAdjusting()) {
                applyBalance(false);
            }
        });

        mainPanel.add(leftLabel);
        mainPanel.add(leftSlider);

        mainPanel.add(Box.createVerticalStrut(20));

        // -----------------------------
        // Right channel
        // -----------------------------

        JLabel rightLabel = new JLabel("Right: 100%");
        rightLabel.setFont(new Font("Arial", Font.BOLD, 16));

        rightSlider = new JSlider(0, 100, 100);
        rightSlider.setMajorTickSpacing(25);
        rightSlider.setMinorTickSpacing(5);
        rightSlider.setPaintTicks(true);
        rightSlider.setPaintLabels(true);

        rightSlider.addChangeListener(e -> {

            rightLabel.setText("Right: " + rightSlider.getValue() + "%");

            if (!rightSlider.getValueIsAdjusting()) {
                applyBalance(false);
            }
        });

        mainPanel.add(rightLabel);
        mainPanel.add(rightSlider);

        mainPanel.add(Box.createVerticalStrut(25));

        // -----------------------------
        // Buttons
        // -----------------------------

        JPanel buttonPanel = new JPanel();

        JButton applyButton = new JButton("Apply");
        JButton resetButton = new JButton("Reset");

        applyButton.addActionListener(e -> applyBalance(true));

        resetButton.addActionListener(e -> {

            leftSlider.setValue(100);
            rightSlider.setValue(100);

            applyBalance(true);
        });

        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);

        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    // =========================================================
    // Apply audio balance
    // =========================================================

    private void applyBalance(boolean showSuccessDialog) {

        int left = leftSlider.getValue();
        int right = rightSlider.getValue();

        Path scriptFile = null;

        try {

            String script = buildPowerShellScript(left, right);

            // Write the script to a temp .ps1 file instead of passing it
            // inline via -Command. Passing a large script containing
            // embedded double quotes as a single command-line argument is
            // fragile on Windows: both ProcessBuilder's argument quoting
            // and PowerShell's own command-line parser rewrite/strip
            // quote characters, which is exactly what turned
            // [Guid("BCDE0395-...")] into [Guid(BCDE0395-...)] and broke
            // the C# compile. A .ps1 file has no such ambiguity.
            scriptFile = Files.createTempFile("audio-balance-", ".ps1");
            Files.write(scriptFile, script.getBytes(StandardCharsets.UTF_8));

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "powershell.exe",
                    "-NoProfile",
                    "-ExecutionPolicy",
                    "Bypass",
                    "-File",
                    scriptFile.toAbsolutePath().toString()
            );

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                    );

            StringBuilder output = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {

                if (showSuccessDialog) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Audio balance applied!\n\n" +
                                    "Left: " + left + "%\n" +
                                    "Right: " + right + "%",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Windows could not change the audio balance.\n\n"
                                + output,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Java Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {

            if (scriptFile != null) {
                try {
                    Files.deleteIfExists(scriptFile);
                } catch (IOException ignored) {
                    // best effort cleanup
                }
            }
        }
    }

    // =========================================================
    // PowerShell + Windows Core Audio
    // =========================================================

    private String buildPowerShellScript(int left, int right) {

        return
                "$left = " + left + " / 100.0\n" +
                        "$right = " + right + " / 100.0\n" +

                        "$source = @'\n" +

                        "using System;\n" +
                        "using System.Runtime.InteropServices;\n" +

                        "public enum EDataFlow\n" +
                        "{\n" +
                        "    eRender = 0,\n" +
                        "    eCapture = 1,\n" +
                        "    eAll = 2\n" +
                        "}\n" +

                        "public enum ERole\n" +
                        "{\n" +
                        "    eConsole = 0,\n" +
                        "    eMultimedia = 1,\n" +
                        "    eCommunications = 2\n" +
                        "}\n" +

                        "[ComImport]\n" +
                        "[Guid(\"BCDE0395-E52F-467C-8E3D-C4579291692E\")]\n" +
                        "public class MMDeviceEnumerator\n" +
                        "{\n" +
                        "}\n" +

                        "[ComImport]\n" +
                        "[Guid(\"A95664D2-9614-4F35-A746-DE8DB63617E6\")]\n" +
                        "[InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                        "public interface IMMDeviceEnumerator\n" +
                        "{\n" +

                        "    int EnumAudioEndpoints(\n" +
                        "        EDataFlow dataFlow,\n" +
                        "        int stateMask,\n" +
                        "        out IMMDeviceCollection devices);\n" +

                        "    int GetDefaultAudioEndpoint(\n" +
                        "        EDataFlow dataFlow,\n" +
                        "        ERole role,\n" +
                        "        out IMMDevice device);\n" +

                        "    int GetDevice(\n" +
                        "        string id,\n" +
                        "        out IMMDevice device);\n" +

                        "    int RegisterEndpointNotificationCallback(\n" +
                        "        IntPtr client);\n" +

                        "    int UnregisterEndpointNotificationCallback(\n" +
                        "        IntPtr client);\n" +

                        "}\n" +

                        "[ComImport]\n" +
                        "[Guid(\"0BD7A1BE-7A1A-44DB-8397-C0B8D5E3E3A4\")]\n" +
                        "[InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                        "public interface IMMDeviceCollection\n" +
                        "{\n" +
                        "}\n" +

                        "[ComImport]\n" +
                        "[Guid(\"D666063F-1587-4E43-81F1-B948E807363F\")]\n" +
                        "[InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                        "public interface IMMDevice\n" +
                        "{\n" +

                        "    int Activate(\n" +
                        "        ref Guid iid,\n" +
                        "        int clsCtx,\n" +
                        "        IntPtr activationParams,\n" +
                        "        out IAudioEndpointVolume endpointVolume);\n" +

                        "    int OpenPropertyStore(\n" +
                        "        int access,\n" +
                        "        IntPtr propertyStore);\n" +

                        "    int GetId(out string id);\n" +

                        "    int GetState(out int state);\n" +

                        "}\n" +

                        "[ComImport]\n" +
                        "[Guid(\"5CDF2C82-841E-4546-9722-0CF74078229A\")]\n" +
                        "[InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                        "public interface IAudioEndpointVolume\n" +
                        "{\n" +

                        "    int RegisterControlChangeNotify(IntPtr notify);\n" +

                        "    int UnregisterControlChangeNotify(IntPtr notify);\n" +

                        "    int GetChannelCount(out uint channelCount);\n" +

                        "    int SetMasterVolumeLevel(float level, Guid eventContext);\n" +

                        "    int SetMasterVolumeLevelScalar(float level, Guid eventContext);\n" +

                        "    int GetMasterVolumeLevel(out float level);\n" +

                        "    int GetMasterVolumeLevelScalar(out float level);\n" +

                        "    int SetChannelVolumeLevel(\n" +
                        "        uint channelNumber,\n" +
                        "        float level,\n" +
                        "        Guid eventContext);\n" +

                        "    int SetChannelVolumeLevelScalar(\n" +
                        "        uint channelNumber,\n" +
                        "        float level,\n" +
                        "        Guid eventContext);\n" +

                        "    int GetChannelVolumeLevel(\n" +
                        "        uint channelNumber,\n" +
                        "        out float level);\n" +

                        "    int GetChannelVolumeLevelScalar(\n" +
                        "        uint channelNumber,\n" +
                        "        out float level);\n" +

                        "    int SetMute(\n" +
                        "        bool mute,\n" +
                        "        Guid eventContext);\n" +

                        "    int GetMute(out bool mute);\n" +

                        "    int GetVolumeStepInfo(IntPtr stepInfo);\n" +

                        "    int VolumeStepUp(Guid eventContext);\n" +

                        "    int VolumeStepDown(Guid eventContext);\n" +

                        "    int QueryHardwareSupport(out uint hardwareSupportMask);\n" +

                        "    int GetVolumeRange(\n" +
                        "        out float minDb,\n" +
                        "        out float maxDb,\n" +
                        "        out float incrementDb);\n" +

                        "}\n" +

                        "public static class AudioController\n" +
                        "{\n" +

                        "    public static void SetBalance(float left, float right)\n" +
                        "    {\n" +

                        "        IMMDeviceEnumerator enumerator =\n" +
                        "            (IMMDeviceEnumerator)new MMDeviceEnumerator();\n" +

                        "        IMMDevice device;\n" +

                        "        int result = enumerator.GetDefaultAudioEndpoint(\n" +
                        "            EDataFlow.eRender,\n" +
                        "            ERole.eMultimedia,\n" +
                        "            out device);\n" +

                        "        if (result != 0)\n" +
                        "            Marshal.ThrowExceptionForHR(result);\n" +

                        "        Guid iid = typeof(IAudioEndpointVolume).GUID;\n" +

                        "        IAudioEndpointVolume volume;\n" +

                        "        result = device.Activate(\n" +
                        "            ref iid,\n" +
                        "            23,\n" +
                        "            IntPtr.Zero,\n" +
                        "            out volume);\n" +

                        "        if (result != 0)\n" +
                        "            Marshal.ThrowExceptionForHR(result);\n" +

                        "        uint channels;\n" +

                        "        volume.GetChannelCount(out channels);\n" +

                        "        if (channels < 2)\n" +
                        "            throw new Exception(\"The selected audio device does not have two channels.\");\n" +

                        "        Guid context = Guid.Empty;\n" +

                        "        volume.SetChannelVolumeLevelScalar(\n" +
                        "            0,\n" +
                        "            left,\n" +
                        "            context);\n" +

                        "        volume.SetChannelVolumeLevelScalar(\n" +
                        "            1,\n" +
                        "            right,\n" +
                        "            context);\n" +

                        "    }\n" +

                        "}\n" +

                        "'@\n" +

                        "Add-Type -TypeDefinition $source\n" +

                        "[AudioController]::SetBalance(" +
                        "[float]$left, " +
                        "[float]$right" +
                        ")\n";
    }

    // =========================================================
    // Main
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            AudioBalancer window = new AudioBalancer();

            window.setVisible(true);
        });
    }
}
package net.ellie.bolt;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.e2k.Rivet;

import net.ellie.portaudiojni.AudioInputStream;
import net.ellie.portaudiojni.PortAudioJNI;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Bolt...");
        Rivet.run();
    }
    //     PortAudioJNI portAudio = new PortAudioJNI();
    //     portAudio.initialize();
    //     List<PortAudioJNI.DeviceInfo> devices = portAudio.enumerateDevices();
        
    //     System.out.println("\n=== Available Audio Devices ===");
    //     for (PortAudioJNI.DeviceInfo device : devices) {
    //         System.out.printf("[%d] %s (Host API: %s, In: %d, Out: %d, Sample Rate: %.2f Hz)%n",
    //                 device.index(),
    //                 device.name(),
    //                 device.hostApi(),
    //                 device.maxInputChannels(),
    //                 device.maxOutputChannels(),
    //                 device.defaultSampleRate());
    //     }

    //     // Interactive device selection
    //     Scanner scanner = new Scanner(System.in);
    //     PortAudioJNI.DeviceInfo selectedDevice = null;
        
    //     while (selectedDevice == null) {
    //         System.out.print("\nEnter device index (or -1 to exit): ");
    //         try {
    //             int deviceIndex = scanner.nextInt();
                
    //             if (deviceIndex == -1) {
    //                 System.out.println("Exiting...");
    //                 portAudio.terminate();
    //                 scanner.close();
    //                 return;
    //             }
                
    //             for (PortAudioJNI.DeviceInfo dev : devices) {
    //                 if (dev.index() == deviceIndex) {
    //                     if (dev.maxInputChannels() > 0) {
    //                         selectedDevice = dev;
    //                         break;
    //                     } else {
    //                         System.out.println("Error: Selected device has no input channels.");
    //                         break;
    //                     }
    //                 }
    //             }
                
    //             if (selectedDevice == null) {
    //                 System.out.println("Invalid device index. Please try again.");
    //             }
    //         } catch (Exception e) {
    //             System.out.println("Invalid input. Please enter a number.");
    //             scanner.nextLine();
    //         }
    //     }
        
    //     scanner.close();
    //     System.out.println("\nUsing input device: " + selectedDevice.name());
    //     System.out.println("Monitoring audio levels (Press Ctrl+C to stop)...\n");

    //     try (AudioInputStream stream = portAudio.openInputStream(selectedDevice.index(), 1,
    //             selectedDevice.defaultSampleRate(), 512)) {
            
    //         stream.start();

    //         int sampleRate = (int) selectedDevice.defaultSampleRate();
    //         int bytesPerSample = 2;
    //         int channels = 1;
    //         int bytesPerFrame = bytesPerSample * channels;

    //         int framesPerChunk = 512;
    //         int bytesPerChunk = framesPerChunk * bytesPerFrame;
    //         byte[] chunkBuffer = new byte[bytesPerChunk];

    //         long lastPrint = System.nanoTime();
    //         long intervalNanos = 1_000_000_000L; // 1 second
    //         long sumAbs = 0;
    //         long sampleCount = 0;

    //         while (true) {
    //             int bytesRead = stream.read(chunkBuffer, 0, bytesPerChunk);
    //             if (bytesRead <= 0) {
    //                 System.err.println("Stream returned no data or error.");
    //                 return;
    //             }

    //             // Accumulate amplitude stats
    //             for (int i = 0; i < bytesRead - 1; i += 2) {
    //                 short sample = (short) ((chunkBuffer[i + 1] << 8) | (chunkBuffer[i] & 0xFF));
    //                 sumAbs += Math.abs(sample);
    //                 sampleCount++;
    //             }

    //             long now = System.nanoTime();
    //             if (now - lastPrint >= intervalNanos) {
    //                 double average = sampleCount > 0 ? (double) sumAbs / sampleCount : 0;
    //                 System.out.printf("[%s] Average amplitude: %.2f%n",
    //                         java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")),
    //                         average);
    //                 lastPrint = now;
    //                 sumAbs = 0;
    //                 sampleCount = 0;
    //             }
    //         }
    //     } catch (IOException e) {
    //         System.err.println("Error during audio recording: " + e.getMessage());
    //     }

    //     portAudio.terminate();
    // }
    
    // private static double calculateAverageAmplitude(byte[] buffer, int length) {
    //     long sum = 0;
    //     int samples = length / 2;
        
    //     for (int i = 0; i < length - 1; i += 2) {
    //         short sample = (short) ((buffer[i + 1] << 8) | (buffer[i] & 0xFF));
    //         sum += Math.abs(sample);
    //     }
        
    //     return samples > 0 ? (double) sum / samples : 0;
    // }
}
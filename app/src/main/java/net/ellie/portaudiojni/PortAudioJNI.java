package net.ellie.portaudiojni;

import java.util.List;

public class PortAudioJNI {
    static {
        System.loadLibrary("portaudio_jni");
    }

    public record DeviceInfo(
        int index,
        String name,
        String hostApi,
        int maxInputChannels,
        int maxOutputChannels,
        double defaultSampleRate
    ) {}

    public native int initialize();
    
    public native void terminate();

    public native List<DeviceInfo> enumerateDevices();
}

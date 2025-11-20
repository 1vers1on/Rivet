#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <portaudio.h>
#include "include/net_ellie_portaudiojni_PortAudioJNI.h"
#include "jni.h"
#include "jni_md.h"

void throwPaException(JNIEnv* env, PaError* err) {
    if (err == NULL || *err == paNoError) {
        return;
    }

    jclass paExceptionClass = (*env)->FindClass(env, "java/lang/RuntimeException");
    if (paExceptionClass != NULL) {
        char errorMessage[256];
        snprintf(errorMessage, sizeof(errorMessage), "PortAudio error: %s", Pa_GetErrorText(*err));
        (*env)->ThrowNew(env, paExceptionClass, errorMessage);
    }
}

JNIEXPORT jint JNICALL Java_net_ellie_portaudiojni_PortAudioJNI_initialize(JNIEnv *env, jobject obj) {
    PaError err = Pa_Initialize();
    if (err != paNoError) {
        throwPaException(env, &err);
        return (jint)err;
    }
    return (jint)err;
}

JNIEXPORT void JNICALL Java_net_ellie_portaudiojni_PortAudioJNI_terminate(JNIEnv *env, jobject obj) {
    PaError err = Pa_Terminate();
    if (err != paNoError) {
        throwPaException(env, &err);
    }
}

JNIEXPORT jobject JNICALL Java_net_ellie_portaudiojni_PortAudioJNI_enumerateDevices(JNIEnv *env, jobject obj) {
    jclass arrayListClass = (*env)->FindClass(env, "java/util/ArrayList");
    jmethodID arrayListInit = (*env)->GetMethodID(env, arrayListClass, "<init>", "()V");
    jobject deviceList = (*env)->NewObject(env, arrayListClass, arrayListInit);
    jmethodID arrayListAdd = (*env)->GetMethodID(env, arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jclass deviceInfoClass = (*env)->FindClass(env, "net/ellie/portaudiojni/PortAudioJNI$DeviceInfo");
    jmethodID deviceInfoInit = (*env)->GetMethodID(env, deviceInfoClass, "<init>", "(ILjava/lang/String;Ljava/lang/String;IID)V");

    PaError err;
    int numDevices = Pa_GetDeviceCount();
    if (numDevices < 0) {
        throwPaException(env, (PaError*)&numDevices);
        return NULL;
    }

    for (int i = 0; i < numDevices; i++) {
        const PaDeviceInfo* deviceInfo = Pa_GetDeviceInfo(i);
        const PaHostApiInfo* hostApiInfo = Pa_GetHostApiInfo(deviceInfo->hostApi);
        jstring name = (*env)->NewStringUTF(env, deviceInfo->name);
        jstring hostApiName = (*env)->NewStringUTF(env, hostApiInfo->name);
        jobject deviceInfoObj = (*env)->NewObject(env, deviceInfoClass, deviceInfoInit,
                                                 (jint)i, name, hostApiName,
                                                 (jint)deviceInfo->maxInputChannels,
                                                 (jint)deviceInfo->maxOutputChannels,
                                                 (jdouble)deviceInfo->defaultSampleRate);
        (*env)->CallBooleanMethod(env, deviceList, arrayListAdd, deviceInfoObj);
        (*env)->DeleteLocalRef(env, name);
        (*env)->DeleteLocalRef(env, hostApiName);
        (*env)->DeleteLocalRef(env, deviceInfoObj);
    }

    return deviceList;
}

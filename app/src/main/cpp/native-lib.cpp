#include <jni.h>
#include <string>
#include <android/log.h>
#include <string.h>

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_izzanfi_tutorial6_JniActivity_stringFromJNI(
    JNIEnv* env,
    jobject) {

    std::string hello = "Hello from C++";

    __android_log_write(ANDROID_LOG_DEBUG, "API123", "Debug Log");

    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_izzanfi_tutorial6_JniActivity_showNameFromEditText(
        JNIEnv* env,
        jobject, jstring name) {
    char resString[20];
    const char *fN = env -> GetStringUTFChars(name, NULL);

    strcpy(resString, fN);
    env -> ReleaseStringUTFChars(name, fN);

    __android_log_write(ANDROID_LOG_DEBUG, "API123", resString);
    return env -> NewStringUTF(resString);
}
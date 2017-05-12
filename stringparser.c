#include <jni.h>
#include <string.h>
#include <math.h>

jstring Java_uk_org_myutcreading_utcr_travel_app_http_string(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env,"Test");

}
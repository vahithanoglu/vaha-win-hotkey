#include "stdafx.h"
#include "hotkey.h"

JNIEXPORT jboolean JNICALL Java_com_vahabilisim_win_hotkey_Hotkey_isKeyPressed(JNIEnv *env, jclass cls, jint key){
	return KEY_PRESSED == GetAsyncKeyState((int)key) ? true : false;
}

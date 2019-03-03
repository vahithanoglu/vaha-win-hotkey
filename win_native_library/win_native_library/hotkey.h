#ifndef _include_vaha_win_hotkey

	#define _include_vaha_win_hotkey

	#define KEY_PRESSED -32768
	
	#ifdef __cplusplus
		extern "C"{
	#endif

		JNIEXPORT jboolean JNICALL Java_com_vahabilisim_win_hotkey_Hotkey_isKeyPressed(JNIEnv *, jclass, jint);
	
	#ifdef __cplusplus
		}
	#endif
#endif


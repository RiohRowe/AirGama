package org.airrowe.game_player.input_emulation;

public enum VKey {
	//Modifier Keys
	SHIFT(0x10),
	CONTROL(0x11),
	ALT(0x12),   // Alt
	SHIFT_LEFT(0xA0),
	SHIFT_RIGHT(0xA1),
	CONTROL_LEFT(0xA2),
	CONTROL_RIGHT(0xA3),
	ALT_LEFT(0xA4),   // Left Alt
	ALT_RIGHT(0xA5),   // Right Alt
	//NAV/Editing Keys
	BACK(0x08),   // Backspace
	TAB(0x09),
	RETURN(0x0D),   // Enter
	ESCAPE(0x1B),
	SPACE (0x20),
	DELETE(0x2E),
	INSERT(0x2D),
	//Arrow Keys
	LEFT(0x25),
	UP(0x26),
	RIGHT(0x27),
	DOWN(0x28),
	//HOME KEYS
	HOME(0x24),
	END(0x23),
	PAGE_UP(0x21),   // Page Up
	PAGE_DOWN(0x22),
	//Function Keys
	F1(0x70),
	F2(0x71),
	F3(0x72),
	F4(0x73),
	F5(0x74),
	F6(0x75),
	F7(0x76),
	F8(0x77),
	F9(0x78),
	F10(0x79),
	F11(0x7A),
	F12(0x7B),
	F13(0x7C),
	F14(0x7D),
	F15(0x7E),
	F16(0x7F),
	F17(0x80),
	F18(0x81),
	F19(0x82),
	F20(0x83),
	F21(0x84),
	F22(0x85),
	F23(0x86),
	F24(0x87),
	//Lock Keys
	CAPS_LOCK(0x14),   // Caps Lock
	NUM_LOCK(0x90),
	SCROLL_LOCK(0x91),
    // Letters (A–Z)
    A(0x41),
    B(0x42),
    C(0x43),
    D(0x44),
    E(0x45),
    F(0x46),
    G(0x47),
    H(0x48),
    I(0x49),
    J(0x4A),
    K(0x4B),
    L(0x4C),
    M(0x4D),
    N(0x4E),
    O(0x4F),
    P(0x50),
    Q(0x51),
    R(0x52),
    S(0x53),
    T(0x54),
    U(0x55),
    V(0x56),
    W(0x57),
    X(0x58),
    Y(0x59),
    Z(0x5A),
    // Number row (0–9)
    DIGIT_0(0x30),
    DIGIT_1(0x31),
    DIGIT_2(0x32),
    DIGIT_3(0x33),
    DIGIT_4(0x34),
    DIGIT_5(0x35),
    DIGIT_6(0x36),
    DIGIT_7(0x37),
    DIGIT_8(0x38),
    DIGIT_9(0x39),
    // Symbol keys (US keyboard)
    MINUS(0xBD),          // - _
    EQUALS(0xBB),         // = +
    LEFT_BRACKET(0xDB),   // [ {
    RIGHT_BRACKET(0xDD),  // ] }
    BACKSLASH(0xDC),      // \ |
    SEMICOLON(0xBA),      // ; :
    APOSTROPHE(0xDE),     // ' "
    COMMA(0xBC),          // , <
    PERIOD(0xBE),         // . >
    SLASH(0xBF),          // / ?
    BACKTICK(0xC0);       // ` ~
	
	
	public int code;
	VKey(int keyCode){
		this.code = keyCode;
	}
}

package com.jphilips.shared.util;

public interface Command<I,O>{
    O execute(I command);
}

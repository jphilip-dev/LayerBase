package com.jphilips.shared.domain.util;

public interface Command<I,O>{
    O execute(I command);
}

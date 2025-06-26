package com.jphilips.shared.util;

public interface Query<I,O>{
    O execute(I query);
}
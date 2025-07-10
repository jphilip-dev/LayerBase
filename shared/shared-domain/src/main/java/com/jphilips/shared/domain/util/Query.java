package com.jphilips.shared.domain.util;

public interface Query<I,O>{
    O execute(I query);
}
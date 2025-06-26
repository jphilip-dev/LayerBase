package com.jphiilips.shared.domain.util;

public interface Query<I,O>{
    O execute(I query);
}
package com.crypto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WaveAction {
    SELL(-1.0),
    BUY(0.998),
    WAIT(0.0),
    SELL_DNO(-1.0);

    private final Double value;
}

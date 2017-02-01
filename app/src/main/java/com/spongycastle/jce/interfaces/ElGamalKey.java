package com.spongycastle.jce.interfaces;

import javax.crypto.interfaces.DHKey;

import com.spongycastle.jce.spec.ElGamalParameterSpec;

public interface ElGamalKey
    extends DHKey
{
    public ElGamalParameterSpec getParameters();
}

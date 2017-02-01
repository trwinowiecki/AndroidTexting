package com.spongycastle.openpgp.operator;

import com.spongycastle.openpgp.PGPException;

public interface PBEProtectionRemoverFactory
{
    PBESecretKeyDecryptor createDecryptor(String protection)
        throws PGPException;
}

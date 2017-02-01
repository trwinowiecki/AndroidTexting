package com.spongycastle.openpgp.bc;

import java.io.IOException;
import java.io.InputStream;

import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPSecretKeyRing;
import com.spongycastle.openpgp.operator.KeyFingerPrintCalculator;
import com.spongycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;

public class BcPGPSecretKeyRing
    extends PGPSecretKeyRing
{
    private static KeyFingerPrintCalculator fingerPrintCalculator = new BcKeyFingerprintCalculator();

    public BcPGPSecretKeyRing(byte[] encoding)
        throws IOException, PGPException
    {
        super(encoding, fingerPrintCalculator);
    }

    public BcPGPSecretKeyRing(InputStream in)
        throws IOException, PGPException
    {
        super(in, fingerPrintCalculator);
    }
}

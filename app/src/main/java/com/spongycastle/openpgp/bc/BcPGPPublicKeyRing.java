package com.spongycastle.openpgp.bc;

import java.io.IOException;
import java.io.InputStream;

import com.spongycastle.openpgp.PGPPublicKeyRing;
import com.spongycastle.openpgp.operator.KeyFingerPrintCalculator;
import com.spongycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;

public class BcPGPPublicKeyRing
    extends PGPPublicKeyRing
{
    private static KeyFingerPrintCalculator fingerPrintCalculator = new BcKeyFingerprintCalculator();

    public BcPGPPublicKeyRing(byte[] encoding)
        throws IOException
    {
        super(encoding, fingerPrintCalculator);
    }

    public BcPGPPublicKeyRing(InputStream in)
        throws IOException
    {
        super(in, fingerPrintCalculator);
    }
}

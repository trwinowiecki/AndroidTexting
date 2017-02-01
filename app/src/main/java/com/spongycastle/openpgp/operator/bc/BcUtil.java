package com.spongycastle.openpgp.operator.bc;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import com.spongycastle.asn1.ASN1ObjectIdentifier;
import com.spongycastle.asn1.x9.ECNamedCurveTable;
import com.spongycastle.asn1.x9.X9ECParameters;
import com.spongycastle.crypto.BlockCipher;
import com.spongycastle.crypto.BufferedBlockCipher;
import com.spongycastle.crypto.ec.CustomNamedCurves;
import com.spongycastle.crypto.io.CipherInputStream;
import com.spongycastle.crypto.modes.CFBBlockCipher;
import com.spongycastle.crypto.modes.OpenPGPCFBBlockCipher;
import com.spongycastle.crypto.params.KeyParameter;
import com.spongycastle.crypto.params.ParametersWithIV;
import com.spongycastle.math.ec.ECCurve;
import com.spongycastle.math.ec.ECPoint;
import com.spongycastle.openpgp.operator.PGPDataDecryptor;
import com.spongycastle.openpgp.operator.PGPDigestCalculator;
import com.spongycastle.util.BigIntegers;

class BcUtil
{
    static BufferedBlockCipher createStreamCipher(boolean forEncryption, BlockCipher engine, boolean withIntegrityPacket, byte[] key)
    {
        BufferedBlockCipher c;

        if (withIntegrityPacket)
        {
            c = new BufferedBlockCipher(new CFBBlockCipher(engine, engine.getBlockSize() * 8));
        }
        else
        {
            c = new BufferedBlockCipher(new OpenPGPCFBBlockCipher(engine));
        }

        KeyParameter keyParameter = new KeyParameter(key);

        if (withIntegrityPacket)
        {
            c.init(forEncryption, new ParametersWithIV(keyParameter, new byte[engine.getBlockSize()]));
        }
        else
        {
            c.init(forEncryption, keyParameter);
        }

        return c;
    }

    public static PGPDataDecryptor createDataDecryptor(boolean withIntegrityPacket, BlockCipher engine, byte[] key)
    {
        final BufferedBlockCipher c = createStreamCipher(false, engine, withIntegrityPacket, key);

        return new PGPDataDecryptor()
        {
            public InputStream getInputStream(InputStream in)
            {
                return new CipherInputStream(in, c);
            }

            public int getBlockSize()
            {
                return c.getBlockSize();
            }

            public PGPDigestCalculator getIntegrityCalculator()
            {
                return new SHA1PGPDigestCalculator();
            }
        };
    }

    public static BufferedBlockCipher createSymmetricKeyWrapper(boolean forEncryption, BlockCipher engine, byte[] key, byte[] iv)
    {
        BufferedBlockCipher c = new BufferedBlockCipher(new CFBBlockCipher(engine, engine.getBlockSize() * 8));

        c.init(forEncryption, new ParametersWithIV(new KeyParameter(key), iv));

        return c;
    }

    static X9ECParameters getX9Parameters(ASN1ObjectIdentifier curveOID)
    {
        X9ECParameters x9 = CustomNamedCurves.getByOID(curveOID);
        if (x9 == null)
        {
            x9 = ECNamedCurveTable.getByOID(curveOID);
        }

        return x9;
    }

    static ECPoint decodePoint(
        BigInteger encodedPoint,
        ECCurve    curve)
        throws IOException
    {
        return curve.decodePoint(BigIntegers.asUnsignedByteArray(encodedPoint));
    }
}

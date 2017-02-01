package com.spongycastle.crypto.util;

import java.io.IOException;

import com.spongycastle.asn1.ASN1OctetString;
import com.spongycastle.asn1.ASN1Primitive;
import com.spongycastle.asn1.DEROctetString;
import com.spongycastle.util.Arrays;

class DerUtil
{
    static ASN1OctetString getOctetString(byte[] data)
    {
        if (data == null)
        {
            return new DEROctetString(new byte[0]);
        }

        return new DEROctetString(Arrays.clone(data));
    }

    static byte[] toByteArray(ASN1Primitive primitive)
    {
        try
        {
            return primitive.getEncoded();
        }
        catch (final IOException e)
        {
            throw new IllegalStateException("Cannot get encoding: " + e.getMessage())
            {
                public Throwable getCause()
                {
                    return e;
                }
            };
        }
    }
}

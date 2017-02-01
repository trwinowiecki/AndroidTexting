package com.spongycastle.crypto;

import java.io.IOException;
import java.io.InputStream;

import com.spongycastle.crypto.params.AsymmetricKeyParameter;

public interface KeyParser
{
    AsymmetricKeyParameter readKey(InputStream stream)
        throws IOException;
}

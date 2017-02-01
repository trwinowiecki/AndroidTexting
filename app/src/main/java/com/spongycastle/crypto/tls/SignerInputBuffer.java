package com.spongycastle.crypto.tls;

import java.io.ByteArrayOutputStream;

import com.spongycastle.crypto.Signer;

class SignerInputBuffer extends ByteArrayOutputStream
{
    void updateSigner(Signer s)
    {
        s.update(this.buf, 0, count);
    }
}
package com.spongycastle.pqc.crypto.gmss;

import com.spongycastle.crypto.Digest;

public interface GMSSDigestProvider
{
    Digest get();
}

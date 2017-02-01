package com.spongycastle.operator.bc;

import com.spongycastle.crypto.engines.AESWrapEngine;
import com.spongycastle.crypto.params.KeyParameter;

public class BcAESSymmetricKeyWrapper
    extends BcSymmetricKeyWrapper
{
    public BcAESSymmetricKeyWrapper(KeyParameter wrappingKey)
    {
        super(AESUtil.determineKeyEncAlg(wrappingKey), new AESWrapEngine(), wrappingKey);
    }
}

package com.spongycastle.operator.bc;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.crypto.Digest;
import com.spongycastle.crypto.Signer;
import com.spongycastle.crypto.signers.RSADigestSigner;
import com.spongycastle.operator.OperatorCreationException;

public class BcRSAContentSignerBuilder
    extends BcContentSignerBuilder
{
    public BcRSAContentSignerBuilder(AlgorithmIdentifier sigAlgId, AlgorithmIdentifier digAlgId)
    {
        super(sigAlgId, digAlgId);
    }

    protected Signer createSigner(AlgorithmIdentifier sigAlgId, AlgorithmIdentifier digAlgId)
        throws OperatorCreationException
    {
        Digest dig = digestProvider.get(digAlgId);

        return new RSADigestSigner(dig);
    }
}

package com.spongycastle.operator.bc;

import java.io.IOException;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import com.spongycastle.crypto.Digest;
import com.spongycastle.crypto.Signer;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;
import com.spongycastle.crypto.signers.DSADigestSigner;
import com.spongycastle.crypto.signers.DSASigner;
import com.spongycastle.crypto.util.PublicKeyFactory;
import com.spongycastle.operator.DigestAlgorithmIdentifierFinder;
import com.spongycastle.operator.OperatorCreationException;

public class BcDSAContentVerifierProviderBuilder
    extends BcContentVerifierProviderBuilder
{
    private DigestAlgorithmIdentifierFinder digestAlgorithmFinder;

    public BcDSAContentVerifierProviderBuilder(DigestAlgorithmIdentifierFinder digestAlgorithmFinder)
    {
        this.digestAlgorithmFinder = digestAlgorithmFinder;
    }

    protected Signer createSigner(AlgorithmIdentifier sigAlgId)
        throws OperatorCreationException
    {
        AlgorithmIdentifier digAlg = digestAlgorithmFinder.find(sigAlgId);
        Digest dig = digestProvider.get(digAlg);

        return new DSADigestSigner(new DSASigner(), dig);
    }

    protected AsymmetricKeyParameter extractKeyParameters(SubjectPublicKeyInfo publicKeyInfo)
        throws IOException
    {
        return PublicKeyFactory.createKey(publicKeyInfo);
    }
}

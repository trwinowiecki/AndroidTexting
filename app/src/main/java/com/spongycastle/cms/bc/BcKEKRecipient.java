package com.spongycastle.cms.bc;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.cms.CMSException;
import com.spongycastle.cms.KEKRecipient;
import com.spongycastle.crypto.CipherParameters;
import com.spongycastle.operator.OperatorException;
import com.spongycastle.operator.SymmetricKeyUnwrapper;
import com.spongycastle.operator.bc.BcSymmetricKeyUnwrapper;

public abstract class BcKEKRecipient
    implements KEKRecipient
{
    private SymmetricKeyUnwrapper unwrapper;

    public BcKEKRecipient(BcSymmetricKeyUnwrapper unwrapper)
    {
        this.unwrapper = unwrapper;
    }

    protected CipherParameters extractSecretKey(AlgorithmIdentifier keyEncryptionAlgorithm, AlgorithmIdentifier contentEncryptionAlgorithm, byte[] encryptedContentEncryptionKey)
        throws CMSException
    {
        try
        {
            return CMSUtils.getBcKey(unwrapper.generateUnwrappedKey(contentEncryptionAlgorithm, encryptedContentEncryptionKey));
        }
        catch (OperatorException e)
        {
            throw new CMSException("exception unwrapping key: " + e.getMessage(), e);
        }
    }
}

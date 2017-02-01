package com.spongycastle.cms.bc;

import com.spongycastle.asn1.ASN1ObjectIdentifier;
import com.spongycastle.asn1.ASN1OctetString;
import com.spongycastle.asn1.pkcs.PBKDF2Params;
import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.cms.CMSException;
import com.spongycastle.cms.PasswordRecipient;
import com.spongycastle.cms.PasswordRecipientInfoGenerator;
import com.spongycastle.crypto.PBEParametersGenerator;
import com.spongycastle.crypto.Wrapper;
import com.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import com.spongycastle.crypto.params.KeyParameter;
import com.spongycastle.crypto.params.ParametersWithIV;
import com.spongycastle.operator.GenericKey;

public class BcPasswordRecipientInfoGenerator
    extends PasswordRecipientInfoGenerator
{
    public BcPasswordRecipientInfoGenerator(ASN1ObjectIdentifier kekAlgorithm, char[] password)
    {
        super(kekAlgorithm, password);
    }

    protected byte[] calculateDerivedKey(int schemeID, AlgorithmIdentifier derivationAlgorithm, int keySize)
        throws CMSException
    {
        PBKDF2Params params = PBKDF2Params.getInstance(derivationAlgorithm.getParameters());
        byte[] encodedPassword = (schemeID == PasswordRecipient.PKCS5_SCHEME2) ? PBEParametersGenerator.PKCS5PasswordToBytes(password) : PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password);

        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator();

        gen.init(encodedPassword, params.getSalt(), params.getIterationCount().intValue());

        return ((KeyParameter)gen.generateDerivedParameters(keySize)).getKey();
    }

    public byte[] generateEncryptedBytes(AlgorithmIdentifier keyEncryptionAlgorithm, byte[] derivedKey, GenericKey contentEncryptionKey)
        throws CMSException
    {
        byte[] contentEncryptionKeySpec = ((KeyParameter)CMSUtils.getBcKey(contentEncryptionKey)).getKey();
        Wrapper keyEncryptionCipher = EnvelopedDataHelper.createRFC3211Wrapper(keyEncryptionAlgorithm.getAlgorithm());

        keyEncryptionCipher.init(true, new ParametersWithIV(new KeyParameter(derivedKey), ASN1OctetString.getInstance(keyEncryptionAlgorithm.getParameters()).getOctets()));

        return keyEncryptionCipher.wrap(contentEncryptionKeySpec, 0, contentEncryptionKeySpec.length);
    }
}

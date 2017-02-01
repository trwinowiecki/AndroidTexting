package com.spongycastle.openpgp.operator.bc;

import com.spongycastle.bcpg.HashAlgorithmTags;
import com.spongycastle.bcpg.PublicKeyAlgorithmTags;
import com.spongycastle.bcpg.SymmetricKeyAlgorithmTags;
import com.spongycastle.crypto.AsymmetricBlockCipher;
import com.spongycastle.crypto.BlockCipher;
import com.spongycastle.crypto.Digest;
import com.spongycastle.crypto.Signer;
import com.spongycastle.crypto.Wrapper;
import com.spongycastle.crypto.digests.MD2Digest;
import com.spongycastle.crypto.digests.MD5Digest;
import com.spongycastle.crypto.digests.RIPEMD160Digest;
import com.spongycastle.crypto.digests.SHA1Digest;
import com.spongycastle.crypto.digests.SHA224Digest;
import com.spongycastle.crypto.digests.SHA256Digest;
import com.spongycastle.crypto.digests.SHA384Digest;
import com.spongycastle.crypto.digests.SHA512Digest;
import com.spongycastle.crypto.digests.TigerDigest;
import com.spongycastle.crypto.encodings.PKCS1Encoding;
import com.spongycastle.crypto.engines.AESEngine;
import com.spongycastle.crypto.engines.AESFastEngine;
import com.spongycastle.crypto.engines.BlowfishEngine;
import com.spongycastle.crypto.engines.CAST5Engine;
import com.spongycastle.crypto.engines.CamelliaEngine;
import com.spongycastle.crypto.engines.DESEngine;
import com.spongycastle.crypto.engines.DESedeEngine;
import com.spongycastle.crypto.engines.ElGamalEngine;
import com.spongycastle.crypto.engines.IDEAEngine;
import com.spongycastle.crypto.engines.RFC3394WrapEngine;
import com.spongycastle.crypto.engines.RSABlindedEngine;
import com.spongycastle.crypto.engines.TwofishEngine;
import com.spongycastle.crypto.signers.DSADigestSigner;
import com.spongycastle.crypto.signers.DSASigner;
import com.spongycastle.crypto.signers.ECDSASigner;
import com.spongycastle.crypto.signers.RSADigestSigner;
import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPPublicKey;

class BcImplProvider
{
    static Digest createDigest(int algorithm)
        throws PGPException
    {
        switch (algorithm)
        {
        case HashAlgorithmTags.SHA1:
            return new SHA1Digest();
        case HashAlgorithmTags.SHA224:
            return new SHA224Digest();
        case HashAlgorithmTags.SHA256:
            return new SHA256Digest();
        case HashAlgorithmTags.SHA384:
            return new SHA384Digest();
        case HashAlgorithmTags.SHA512:
            return new SHA512Digest();
        case HashAlgorithmTags.MD2:
            return new MD2Digest();
        case HashAlgorithmTags.MD5:
            return new MD5Digest();
        case HashAlgorithmTags.RIPEMD160:
            return new RIPEMD160Digest();
        case HashAlgorithmTags.TIGER_192:
            return new TigerDigest();
        default:
            throw new PGPException("cannot recognise digest");
        }
    }

    static Signer createSigner(int keyAlgorithm, int hashAlgorithm)
        throws PGPException
    {
        switch(keyAlgorithm)
        {
        case PublicKeyAlgorithmTags.RSA_GENERAL:
        case PublicKeyAlgorithmTags.RSA_SIGN:
            return new RSADigestSigner(createDigest(hashAlgorithm));
        case PublicKeyAlgorithmTags.DSA:
            return new DSADigestSigner(new DSASigner(), createDigest(hashAlgorithm));
        case PublicKeyAlgorithmTags.ECDSA:
            return new DSADigestSigner(new ECDSASigner(), createDigest(hashAlgorithm));
        default:
            throw new PGPException("cannot recognise keyAlgorithm: " + keyAlgorithm);
        }
    }

    static BlockCipher createBlockCipher(int encAlgorithm)
        throws PGPException
    {
        BlockCipher engine;

        switch (encAlgorithm)
        {
        case SymmetricKeyAlgorithmTags.AES_128:
        case SymmetricKeyAlgorithmTags.AES_192:
        case SymmetricKeyAlgorithmTags.AES_256:
            engine = new AESEngine();
            break;
        case SymmetricKeyAlgorithmTags.CAMELLIA_128:
        case SymmetricKeyAlgorithmTags.CAMELLIA_192:
        case SymmetricKeyAlgorithmTags.CAMELLIA_256:
            engine = new CamelliaEngine();
            break;
        case SymmetricKeyAlgorithmTags.BLOWFISH:
            engine = new BlowfishEngine();
            break;
        case SymmetricKeyAlgorithmTags.CAST5:
            engine = new CAST5Engine();
            break;
        case SymmetricKeyAlgorithmTags.DES:
            engine = new DESEngine();
            break;
        case SymmetricKeyAlgorithmTags.IDEA:
            engine = new IDEAEngine();
            break;
        case SymmetricKeyAlgorithmTags.TWOFISH:
            engine = new TwofishEngine();
            break;
        case SymmetricKeyAlgorithmTags.TRIPLE_DES:
            engine = new DESedeEngine();
            break;
        default:
            throw new PGPException("cannot recognise cipher");
        }

        return engine;
    }

    static Wrapper createWrapper(int encAlgorithm)
        throws PGPException
    {
        switch (encAlgorithm)
        {
        case SymmetricKeyAlgorithmTags.AES_128:
        case SymmetricKeyAlgorithmTags.AES_192:
        case SymmetricKeyAlgorithmTags.AES_256:
            return new RFC3394WrapEngine(new AESFastEngine());
        case SymmetricKeyAlgorithmTags.CAMELLIA_128:
        case SymmetricKeyAlgorithmTags.CAMELLIA_192:
        case SymmetricKeyAlgorithmTags.CAMELLIA_256:
            return new RFC3394WrapEngine(new CamelliaEngine());
        default:
            throw new PGPException("unknown wrap algorithm: " + encAlgorithm);
        }
    }

    static AsymmetricBlockCipher createPublicKeyCipher(int encAlgorithm)
        throws PGPException
    {
        AsymmetricBlockCipher c;

        switch (encAlgorithm)
        {
        case PGPPublicKey.RSA_ENCRYPT:
        case PGPPublicKey.RSA_GENERAL:
            c = new PKCS1Encoding(new RSABlindedEngine());
            break;
        case PGPPublicKey.ELGAMAL_ENCRYPT:
        case PGPPublicKey.ELGAMAL_GENERAL:
            c = new PKCS1Encoding(new ElGamalEngine());
            break;
        case PGPPublicKey.DSA:
            throw new PGPException("Can't use DSA for encryption.");
        case PGPPublicKey.ECDSA:
            throw new PGPException("Can't use ECDSA for encryption.");
        case PGPPublicKey.ECDH:
            throw new PGPException("Not implemented.");
        default:
            throw new PGPException("unknown asymmetric algorithm: " + encAlgorithm);
        }

        return c;
    }
}

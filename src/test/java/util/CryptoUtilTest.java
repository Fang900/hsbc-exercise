package util;

import org.junit.Assert;
import org.junit.Test;

public class CryptoUtilTest {
    @Test
    public void md5EncryptionTestWithSamePassword(){
        String password1 = "password";
        String password2 = "password";
        String cryptoPwd1 = CryptoUtil.md5Encryption(password1);
        String cryptoPwd2 = CryptoUtil.md5Encryption(password2);
        Assert.assertEquals(cryptoPwd1, cryptoPwd2);
    }

    @Test
    public void md5EncryptionTestWithDifferentPassword(){
        String password1 = "password1";
        String password2 = "password2";
        String cryptoPwd1 = CryptoUtil.md5Encryption(password1);
        String cryptoPwd2 = CryptoUtil.md5Encryption(password2);
        Assert.assertNotEquals(cryptoPwd1, cryptoPwd2);
    }
}

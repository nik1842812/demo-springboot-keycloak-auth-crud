package sn.malcolm.demo.core.helper;

public class AppUtil {
    public static String generateRandomPassword(Integer length) {
        if (length == null) {
            length = 10;
        }
        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        String majs= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String mins = "abcdefghijklmnopqrstuvxyz";

        String specials = "@!$*";

        String numerics = "0123456789";
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            if (i ==2) {
                int index
                        = (int)(specials.length()
                        * Math.random());
                sb.append(specials
                        .charAt(index));
            } else if (i == 3) {
                int index
                        = (int)(numerics.length()
                        * Math.random());
                sb.append(numerics
                        .charAt(index));
            } else if (i ==4) {
                int index
                        = (int)(majs.length()
                        * Math.random());
                sb.append(majs
                        .charAt(index));
            } else if (i ==5) {
                int index
                        = (int)(mins.length()
                        * Math.random());
                sb.append(mins
                        .charAt(index));
            }
            else {
                int index
                        = (int)(AlphaNumericString.length()
                        * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString
                        .charAt(index));
            }

        }

        return sb.toString();
    }
}

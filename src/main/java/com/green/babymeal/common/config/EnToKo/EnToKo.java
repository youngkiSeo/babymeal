package com.green.babymeal.common.config.EnToKo;

public class EnToKo {
    static enum CodeType { chosung, jungsung, jongsung }
    static String ignoreChars = "`1234567890-=[]\\;',./~!@#$%^&*()_+{}|:\"<>? ";
    public static String engToKor(String eng) { StringBuffer sb = new StringBuffer();
        int initialCode = 0, medialCode = 0, finalCode = 0;
        int tempMedialCode, tempFinalCode; for (int i = 0; i < eng.length(); i++) {
            if(ignoreChars.indexOf(eng.substring(i, i + 1)) > -1){
                sb.append(eng.substring(i, i + 1));
                continue;
            }
            initialCode = getCode(CodeType.chosung, eng.substring(i, i + 1));
            i++;
            tempMedialCode = getDoubleMedial(i, eng);
            if (tempMedialCode != -1) {
                medialCode = tempMedialCode; i += 2;
            } else {
                medialCode = getSingleMedial(i, eng);
                i++;
            }
            tempFinalCode = getDoubleFinal(i, eng);
            if (tempFinalCode != -1) {
                finalCode = tempFinalCode;
                tempMedialCode = getSingleMedial(i + 2, eng);
                if (tempMedialCode != -1) {
                    finalCode = getSingleFinal(i, eng);
                } else {
                    i++;
                }
            } else {
                tempMedialCode = getSingleMedial(i + 1, eng);
                if (tempMedialCode != -1) {
                    finalCode = 0;
                    i--;
                }
                else {
                    finalCode = getSingleFinal(i, eng);
                    if (finalCode == -1){
                        finalCode = 0; i--;
                    }
                }
            }
            sb.append((char) (0xAC00 + initialCode + medialCode + finalCode));
        }
        return sb.toString();
    }
    static private int getCode(CodeType type, String c) {
        String init = "rRseEfaqQtTdwWczxvg";
        String[] mid = { "k", "o", "i", "O", "j", "p", "u", "P", "h", "hk", "ho", "hl", "y", "n", "nj", "np", "nl", "b", "m", "ml", "l" };
        String[] fin = { "r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "qt", "t", "T", "d", "w", "c", "z", "x", "v", "g" };
        switch (type) {
            case chosung:
                int index = init.indexOf(c);
                if (index != -1) { return index * 21 * 28; }
                break;
            case jungsung:
                for (int i = 0; i < mid.length; i++) {
                    if (mid[i].equals(c)) {
                        return i * 28;
                    }
                }
                break;
            case jongsung:
                for (int i = 0; i < fin.length; i++) {
                    if (fin[i].equals(c)) {
                        return i + 1;
                    }
                }
                break;
            default:
        }
        return -1;
    }
    static private int getSingleMedial(int i, String eng) {
        if ((i + 1) <= eng.length()) {
            return getCode(CodeType.jungsung, eng.substring(i, i + 1));
        } else {
            return -1;
        }
    }
    static private int getDoubleMedial(int i, String eng) {
        int result; if ((i + 2) > eng.length()) {
            return -1;
        } else {
            result = getCode(CodeType.jungsung, eng.substring(i, i + 2));
            if (result != -1) { return result; } else { return -1;
            }
        }
    }
    static private int getSingleFinal(int i, String eng) {
        if ((i + 1) <= eng.length()) {
            return getCode(CodeType.jongsung, eng.substring(i, i + 1));
        } else {
            return -1;
        }
    }
    static private int getDoubleFinal(int i, String eng) {
        if ((i + 2) > eng.length()) {
            return -1;
        } else {
            return getCode(CodeType.jongsung, eng.substring(i, i + 2));
        }
    }

}


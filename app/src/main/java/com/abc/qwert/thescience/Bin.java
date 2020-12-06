package com.abc.qwert.thescience;

class ToArray {

    static String[] a = new String[5];

    static String bin = "";
    static String oct = "";
    static String dec = "";
    static String hex = "";
    static String str = "";

    static void toArr() {
        a[0] = bin;
        a[1] = oct;
        a[2] = dec;
        a[3] = hex;
        a[4] = str;

    }

    static String delZero(String sca) {
        while(sca.charAt(0) == '0' & sca.length() > 1) {
            sca = sca.substring(1, sca.length());
        }

        return sca;
    }

    static void clearAll() {
        bin = "";
        oct = "";
        dec = "";
        hex = "";
        str = "";

    }

    static String toOct() {
        String oct = "";
        String longBin = bin;

        while(longBin.length() % 3 != 0) {
            longBin = "0" + longBin;
        }

        for(int i=0;i<longBin.length() / 3;i++) {
            long currPow = 1;
            long currDec = 0;
            String currBin = longBin.substring(i * 3, i * 3 + 3);
            for(int j=currBin.length()-1;j>=0;j--) {
                if(currBin.charAt(j) == '1') {
                    currDec += currPow;
                }
                currPow *= 2;
            }

            oct += currDec;
        }

        return oct;
    }

    static String toDec() {
        String dec = "";
        long currPow = 1;
        long currDec = 0;

        for(int i=bin.length()-1;i>=0;i--) {
            if(bin.charAt(i) == '1') {
                currDec += currPow;
            }
            currPow *= 2;
        }

        dec += currDec;

        return dec;
    }

    static String toHex() {
        String hex = "";
        String longBin = bin;

        while(longBin.length() % 4 != 0) {
            longBin = "0" + longBin;
        }

        for(int i=0;i<longBin.length() / 4;i++) {
            long currPow = 1;
            long currDec = 0;
            String currBin = longBin.substring(i * 4, i * 4 + 4);

            for(int j=currBin.length()-1;j>=0;j--) {
                if(currBin.charAt(j) == '1') {
                    currDec += currPow;
                }
                currPow *= 2;
            }

            if(currDec > 9) {
                hex += ((char) ('7' + currDec));
            } else {
                hex += currDec;
            }

        }

        return hex;
    }

    static String toStr() {
        String str = "";
        String longBin = bin;

        while(longBin.length() % 8 != 0) {
            longBin = "0" + longBin;
        }

        for(int i=0;i<longBin.length() / 8;i++) {
            long currPow = 1;
            long currDec = 0;
            String currBin = longBin.substring(i * 8, i * 8 + 8);

            for(int j=currBin.length()-1;j>=0;j--) {
                if(currBin.charAt(j) == '1') {
                    currDec += currPow;
                }
                currPow *= 2;
            }

            str += (char) currDec;
        }

        return str;
    }


}

class Bin extends ToArray{
	
	Bin(String bin){
		clearAll();

		Bin.bin = delZero(bin);
		
		oct = toOct();
		dec = toDec();
		hex = toHex();
		str = toStr();
		
		toArr();
		
	}

}

class Oct extends ToArray{

    private static String toBin() {
        for(int i=0;i<oct.length();i++) {
            String currBin = "";
            int currDec = Character.getNumericValue(oct.charAt(i));

            while(currDec / 2 >= 1) {
                currBin = currDec % 2 + currBin;
                currDec /= 2;
            }

            currBin = currDec + currBin;

            while(currBin.length() % 3 != 0) currBin = "0" + currBin;

            bin += currBin;
        }

        return bin;
    }

    Oct(String oct) {

        clearAll();

        Oct.oct = delZero(oct);

        bin = toBin();
        dec = toDec();
        hex = toHex();
        str = toStr();

        toArr();

    }

}

class Dec extends ToArray{

    private static String toBin() {
        long currDec = Long.parseLong(dec);

        while(currDec / 2 >= 1) {
            bin = currDec % 2 + bin;
            currDec /= 2;
        }

        bin = currDec + bin;

        return bin;
    }

    Dec(String dec){

        clearAll();

        Dec.dec = delZero(dec);

        bin = toBin();
        oct = toOct();
        hex = toHex();
        str = toStr();

        toArr();

    }

}

class Hex extends ToArray{

    private static String toBin() {
        for(int i=0;i<hex.length();i++) {
            String currBin = "";
            long currDec;
            if(Character.getNumericValue(hex.charAt(i)) < 10) {
                currDec = Character.getNumericValue(hex.charAt(i));
            } else {
                currDec = hex.charAt(i) - '7';
            }

            while(currDec / 2 >= 1) {
                currBin = currDec % 2 + currBin;
                currDec /= 2;
            }

            currBin = currDec + currBin;

            while(currBin.length() % 4 != 0) {
                currBin = "0" + currBin;
            }

            bin += currBin;
        }

        return bin;
    }

    Hex(String hex){

        clearAll();

        Hex.hex = delZero(hex);

        bin = toBin();
        oct = toOct();
        dec = toDec();
        str = toStr();

        toArr();

    }

}

class Str extends ToArray{

    private static String toBin() {
        for(int i=0;i<str.length();i++) {
            String currBin = "";
            long currDec = (int) str.charAt(i);

            while(currDec / 2 >= 1) {
                currBin = currDec % 2 + currBin;
                currDec /= 2;
            }

            currBin = currDec + currBin;

            while(currBin.length() % 8 != 0) {
                currBin = "0" + currBin;
            }

            bin += currBin;
        }

        return bin;
    }

    Str(String str){

        clearAll();

        Str.str = str;

        bin = toBin();
        oct = toOct();
        dec = toDec();
        hex = toHex();

        toArr();

    }

}
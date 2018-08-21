package blueline;

import java.util.ArrayList;
import java.util.Arrays;

public class Method {
    public String notation_plain;
    public String notation_bob;
    public String notation_single;
    public int stage;
    public int lead_length;
    
    public Method(int stage, String notation_plain, String notation_bob, String notation_single) {
        this.stage = stage;
        this.notation_plain = notation_plain;
        this.notation_bob = notation_bob;
        this.notation_single = notation_single;
        this.lead_length = Method.DecodePlaceNotation(this.notation_plain).length;
    }
    
    public Course GeneratePlainCourse() {
        return this.GenerateCourse( "m" );
    }
    
    public Course GenerateTouch(String composition) {
        return this.GenerateCourse(composition);
    }
    
    private Course GenerateCourse(String composition) {
        Course course = new Course(this);
        String[] decoded_notation = Method.DecodePlaceNotation(this.notation_plain);
        String[] decoded_bob_notation = Method.DecodePlaceNotation(this.notation_bob);
        String[] decoded_single_notation = Method.DecodePlaceNotation(this.notation_single);
        
        course.changes.add(Change.rounds(this.stage));
        
        boolean started = false;
        
        int leads_so_far = 0;
        
        while (!course.GetLastChange().Equals(Change.rounds(this.stage)) || !started) {
            started = true;
            
            // compute how many changes to take off
            char lead_end_type = composition.charAt(leads_so_far % composition.length());
            int changes_removed = 0;
            
            if(lead_end_type == 'm') {
                changes_removed = 0;
            }
            
            if(lead_end_type == 'b') {
                changes_removed = decoded_bob_notation.length;
            }
            
            if(lead_end_type == 's') {
                changes_removed = decoded_single_notation.length;
            }
            
            // add the plain course section
            for (int i = 0; i < decoded_notation.length - changes_removed; i ++) {
                course.changes.add(course.GetLastChange().ApplyPlaceNotation(decoded_notation[i]));
            }
            
            // add the bob leads (if needed)
            if(lead_end_type == 'm') {
                course.changes.get(course.changes.size() - 1).call = "m";
            }
            if(lead_end_type == 'b') {
                for (String i : decoded_bob_notation) {
                    course.changes.add(course.GetLastChange().ApplyPlaceNotation(i));
                    course.GetLastChange().call = "b";
                }
            }
            if(lead_end_type == 's') {
                for (String i : decoded_single_notation) {
                    course.changes.add(course.GetLastChange().ApplyPlaceNotation(i));
                    course.GetLastChange().call = "s";
                }
            }
            
            leads_so_far += 1;
        }
        
        return course;
    }
    
    static String[] DecodePlaceNotation(String notation) {
        // set up variables
        notation = notation.toUpperCase();
        
        ArrayList<String> decodedNotation;
        decodedNotation = new ArrayList<>();
        
        String numOn = "";

        // decode notation
        for (int i = 0; i < notation.length(); i++) {
            String notationAtI = String.valueOf(notation.charAt(i));
            
            if (Constants.numbers.contains(notationAtI)) {
                numOn += notationAtI;
            }
            
            if (Constants.symbols.contains(notationAtI)) {
                if (!"".equals(numOn)) {
                    decodedNotation.add(numOn);
                }
                
                numOn = "";
                
                if ("X".equals(notationAtI) || "-".equals(notationAtI)) {
                    decodedNotation.add("X");
                }
                
                if (",".equals(notationAtI)) {
                    decodedNotation.add("leadEnd");
                }
            }
        }
        
        decodedNotation.add(numOn);
        
        if (decodedNotation.contains("leadEnd")) {
            if ("leadEnd".equals(decodedNotation.get(decodedNotation.size() - 2))) {
                int middle = decodedNotation.size() - 2;
                
                for (int i = 0; i < middle - 1; i++) {
                    decodedNotation.add(middle, decodedNotation.get(i));
                }
                
                decodedNotation.remove(decodedNotation.size() - 2);
            }
            
            if ("leadEnd".equals(decodedNotation.get(1))) {
                decodedNotation.remove(1);
                
                int size = decodedNotation.size();
                
                for (int i = 1; i < size - 1; i++) {
                    decodedNotation.add(decodedNotation.get(size - i - 1));
                }
            }
        }
        
        String[] output = new String[decodedNotation.size()];
        
        decodedNotation.toArray(output);
        
        return output;
    }
    
    static Method cambridge(int n) {
        if( n == 6 ) {return new Method( 6 , "x36x14x12x36x14x56,12" , "14" , "1234" );}
        if( n == 8 ) {return new Method( 8 , "x38x14x1258x36x14x58x16x78,12" , "14" , "1234" );}
        if( n == 10 ) {return new Method( 10 , "x30x14x1250x36x1470x58x16x70x18x90,12" , "14" , "1234" );}
        if( n == 12 ) {return new Method( 12 , "x3Tx14x125Tx36x147Tx58x169Tx70x18x9Tx10xET,12" , "14" , "1234" );}
        
		return null;
    }
    static Method plain_bob(int n) {
        if( n == 4 ) {return new Method( 6 , "x14x14,12" , "145" , "1234" );}
        if( n == 5 ) {return new Method( 5 , "5.1.5.1.5,12" , "145" , "1234" );}
        if( n == 6 ) {return new Method( 6 , "x16x16x16,12" , "145" , "1234" );}
        if( n == 7 ) {return new Method( 7 , "7.1.7.1.7.1.7,12" , "145" , "1234" );}
        if( n == 8 ) {return new Method( 8 , "x18x18x18x18,12" , "145" , "1234" );}
        if( n == 9 ) {return new Method( 9 , "9.1.9.1.9.1.9.1.9,12" , "145" , "1234" );}
        if( n == 10 ) {return new Method( 10 , "x10x10x10x10x10,12" , "145" , "1234" );}
        if( n == 11 ) {return new Method( 11 , "A.1.A.1.A.1.A.1.A.1.A,12" , "145" , "1234" );}
        if( n == 12 ) {return new Method( 12 , "x1Bx1Bx1Bx1Bx1Bx1B,12" , "145" , "1234" );}
        
		return null;
    }
    static Method grandsire(int n) {
        if( n == 5 ) {return new Method( 5 , "3,1.5.1.5.1" , "3.1" , "3.123" );}
        if( n == 7 ) {return new Method( 7 , "3,1.7.1.7.1.7.1" , "3.1" , "3.123" );}
        if( n == 9 ) {return new Method( 9 , "3,1.9.1.9.1.9.1.9.1" , "3.1" , "3.123" );}
        if( n == 11 ) {return new Method( 11 , "3,1.A.1.A.1.A.1.A.1.A.1" , "3.1" , "3.123" );}
        
		return null;
    }
    static Method stedman(int n) {
        if( n == 5 ) {return new Method( 5 , "3.1.5.3.1.3.1.3.5.1.3.1" , "1" , "45" );}
        if( n == 7 ) {return new Method( 7 , "3.1.7.3.1.3.1.3.7.1.3.1" , "5" , "567" );}
        if( n == 9 ) {return new Method( 9 , "3.1.9.3.1.3.1.3.9.1.3.1" , "7" , "789" );}
        if( n == 11 ) {return new Method( 11 , "3.1.A.3.1.3.1.3.A.1.3.1" , "9" , "90A" );}
        
		return null;
    }
    static Method treble_place_hunting(int n) {
        if( n == 5 ) {return new Method( 5 , "3.5.3.1" , "125" , "145" );}
        if( n == 9 ) {return new Method( 9 , "5.9.5.1" , "125" , "145" );}
        
		return null;
    }
}

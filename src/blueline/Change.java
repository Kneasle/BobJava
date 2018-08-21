package blueline;

public class Change {
    int[] sequence;
    int stage;
    String call;
    int repeats;
    
    public Change( int[] sequence ) {
        this.sequence = sequence;
        this.stage = sequence.length;
        this.repeats = 0;
        this.call = " ";
    }
    
    static Change rounds( int size ) {
        int[] output = new int[size];
        
        for(int i = 0; i < size; i ++) {
            output[i] = i;
        }
        
        return new Change(output);
    }
    
    public void swapBells( int first_place ) {
        int bell1 = this.sequence[first_place];
        int bell2 = this.sequence[first_place + 1];
        
        this.sequence[first_place + 1] = bell1;
        this.sequence[first_place] = bell2;
    }
    
    public Change ApplyPlaceNotation( String notation ) {
        // NOTE: Just returns the same sequence
        Change next_change = this.ClonedChange();
        
        if( "X".equals(notation) ) {
            for( int i = 0; i < next_change.stage; i += 2 ) {
                next_change.swapBells(i);
            }
        } else {
            int j = 1;
            
            while(j < this.stage) {
                if( 
                        !notation.contains(Character.toString(Constants.numbers.charAt(j - 1)))
                        &&
                        !notation.contains(Character.toString(Constants.numbers.charAt(j)))
                ) {
                    next_change.swapBells(j - 1);

                    j += 1;
                }

                j += 1;
            }
        }
        
        return next_change;
    }
    
    public boolean Equals( Change other ) {
        if( other.stage != this.stage ) {
            return false;
        }
        
        for( int i = 0; i < this.stage; i ++ ) {
            if( other.sequence[i] != this.sequence[i] ) {
                return false;
            }
        }
        
        return true;
    }
    
    public Change ClonedChange() {
        return new Change( this.sequence.clone() );
    }
    
    public float TotalMusicality() {
        float total_musicality = 0;
        
        for(int step = 1; step < this.sequence.length / 2; step ++) {
            for(int i = 0; i < this.stage - step; i ++) {
                float difference = (float)Math.abs(this.sequence[i] - this.sequence[i + step]);

                total_musicality += 1f / ( difference + ((float)step - 1f) * 0.6f );
            }
        }
        
        return total_musicality;
    }
    
    public float Musicality() {
        return TotalMusicality() / Change.rounds(this.stage).TotalMusicality();
    }
    
    public String StringRepeats() {
        if(this.repeats == 0) {
            return " ";
        } else {
            return String.valueOf(this.repeats);
        }
    }
    
    @Override
    public String toString() {
        String output = this.call;
        
        for(int i = 0; i < this.sequence.length; i ++) {
            output += Constants.numbers.charAt(this.sequence[i]);
        }
        
        output += " " + this.StringRepeats() + " " + this.Musicality();
        
        return output;
    }
}

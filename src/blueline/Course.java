package blueline;

import java.util.ArrayList;

public class Course {
    public Method method;
    public ArrayList<Change> changes = new ArrayList<> ();
    public int repeats;
    
    public Course(Method method) {
        this.method = method;
    }
    
    public Change GetLastChange() {
        return this.changes.get(this.changes.size() - 1);
    }
    
    public int GetNumberOfLeads() {
        return this.changes.size() / method.lead_length;
    }
    
    public float GetMusicality() {
        float total_music = 0f;
        
        for(int i = 0; i < changes.size() - 1; i ++) {
            total_music += changes.get(i).Musicality();
        }
        
        float music = total_music / (float)(changes.size() - 1);
        
        return (float)Math.pow( Math.E , music / 0.41f);
    }
    
    public String GetDataAsString() {
        return "{changes: " + (this.changes.size() - 1) + ", leads: " + 
                this.GetNumberOfLeads() + " (" + this.method.lead_length + " changes each), truth: " + 
                this.IsTrue() + " (" + this.repeats + " repeats), musicality: " + 
                this.GetMusicality() + "}";
    }
    
    public boolean IsTrue() {
        this.repeats = 0;
        for(int i = 0; i < this.changes.size() - 1; i ++) {
            this.changes.get(i).repeats = 0;
        }
        for(int i = 0; i < this.changes.size() - 1; i ++) {
            for(int j = 0; j < i; j ++) {
                if( this.changes.get(i).Equals( this.changes.get(j) ) ) {
                    this.changes.get(i).repeats += 1;
                    this.changes.get(j).repeats += 1;
                    this.repeats += 1;
                }
            }
        }
        
        return this.repeats == 0;
    }
    
    @Override
    public String toString() {
        this.IsTrue();
        
        String output = "";
        
        for (int i = 0; i < changes.size(); i ++) {
            output += changes.get(i).toString() + "\n";
            if((i + 1) % method.lead_length == 0) {
                output += "\n";
            }
        }
        
        return output;
    }
    
    public static String[] GenerateAllTouches( int length ) {
        ArrayList<String> touches = new ArrayList<> ();
        
        if(length == 1) {
            return new String[3];
        }
        
        for( int i = 0; i < 3; i ++ ) {
            String touch_symbol = "mbs".substring(i, i + 1);
            
            for( int j = 0; j < 3; j ++ ) {
                String[] recent_touches = Course.GenerateAllTouches(length - 1);
                
                for (String recent_touch : recent_touches) {
                    touches.add(touch_symbol.concat(recent_touch));
                    System.out.println(recent_touch);
                }
            }
        }
        
        String[] output = new String[touches.size()];
        
        touches.toArray( output );
        
        return output;
    }
}

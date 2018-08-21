package blueline;

public class Blueline {
    public static void main(String[] args) {
        Method meth = Method.plain_bob(5);
        
        Course final_course = new Course( meth );
        
        String[] touches = Course.GenerateAllTouches(3);
        
        for( int i = 0; i < touches.length; i ++ ) {
            System.out.println(touches[i]);
        }
        
        for( int i = 0; i < 5; i ++ ) {
            Course course = meth.GenerateTouch("m");
        }
        
        System.out.println(final_course);
        
        System.out.println(final_course.GetDataAsString());

    }
}

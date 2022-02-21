package search;

public class BinarySearch{

    // Pred: i < j => a[i] < a[j]
    // Post: R = (min i, a[i] <= x) || (a.length - if index doesn't exists)
    public static int iterativeSearch(int x, int[] a) {
        // (-1 == -1 || a[-1] > x) && (a.length == a.length || a[a.length] <= x)
        int l = -1, r = a.length;
        //Inv: (l' == -1 || a[l'] > x) && (r' == a.length || a[r'] <= x) 
        while (r - l > 1) {
            // Inv && (r' - l' > 1)
            // Inv && (r' - l' > 1) && (l' < (l' + r')/2 < r')
            int m = (l + r)/2;
            // Inv && (r' - l' > 1) && (l' < m' < r')
            if (a[m] > x) {
                // Inv && (r' - l' > 1) && (l' < m' < r') && (a[m'] > x)
                // (a[m'] > x) && (r' == a.length || a[r'] <= x)
                l = m;
                // (a[l'] > x) && (r' == a.length || a[r'] <= x)
            }
            else {
                // Inv && (r' - l' > 1) && (l' < m' < r') && (a[m'] <= x)
                // (l' == -1 || a[l'] > x) && (a[m'] <= x)
                r = m;
                // (l' == -1 || a[l'] > x) && (a[r'] <= x)
            }
            // Inv
        }
        // (l' == -1 || a[l'] > x) && (r' == a.length || a[r'] <= x) && (r' - l' <= 1)
        // ((a[r'] <= x) && (r' == 0 || a[r' - 1] > x)) || (r' == a.length) - index doesn't exists
        return r;
    }

    // Pred: i < j => a[i] >= a[j]
    // Post: R = (min i, a[i] <= x) || (a.length - if index doesn't exists)
    public static int recursiveSearch(int x, int[] a, int l, int r) {
        // Inv: (l' == -1 || a[l'] > x) && (r' == a.length || a[r'] <= x)
        if (r - l <= 1) {
            // Inv && (r' - l' <= 1)
            // ((a[r'] <= x) && (r' == 0 || a[r' - 1] > x)) || (r' == a.length) - index doesn't exists
            return r;
        }
        // Inv && (r' - l' > 1)
        // Inv && (r' - l' > 1) && (l' < (l' + r')/2 < r')
        int m = (l + r)/2;
        // Inv && (r' - l' > 1) && (l' < m' < r')
        if (a[m] > x) {
            // Inv && (r' - l' > 1) && (l' < m' < r') && (a[m'] > x)
            // (a[m'] > x) && (r' == a.length || a[r'] <= x)
            return recursiveSearch(x, a, m, r);
        }
        else {
            // Inv && (r' - l' > 1) && (l' < m' < r') && (a[m'] <= x)
            // (l' == -1 || a[l'] > x) && (a[m'] <= x)
            return recursiveSearch(x, a, l, m);
        }
    }
    
    // Pred: (i < j && i > 0) => args[i] >= args[j]
    public static void main(String[] args){
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
        }
        System.out.println(recursiveSearch(x, a, -1, a.length));
        //System.out.println(iterativeSearch(x, a));
    }
}
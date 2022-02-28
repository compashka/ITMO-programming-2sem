package search;

public class BinarySearchMin {

    // :NOTE: Не ясно, кто такие i, j, f, k, l, какие у них диапазоны и каким кванторами они связаны (исправлено)
    /* Pred: The only one f: 0 <= i < j <= f: => arr[i] > arr[j] && 
                             arr[f] <= arr[f+1] && (f + 1) <= ii < jj < arr.lenght: => arr[ii] < arr[jj]
    */ 
    // Post: for (int i = 0; i < arr.length; i++) R <= arr[i]
    public static int iterativeSearch(int[] a) {
        // (R <= a[0]) && (a.length == a.length || R <= a[a.length]) && (0 <= index(R) < a.length)
        int l = 0, r = a.length;
        // Inv: (R <= a[l']) && (r' == a.length || R <= a[r']) && (l' <= index(R) < r')
        while (r - l > 1) {
            // Inv && (r' - l' > 1)
            // Inv && (r' - l' > 1) && (l' < (l' + r')/2 < r')
            int m = (l + r)/2;
            // Inv && (r' - l' > 1) && (l' < m' < r')
            if (a[m] > a[m - 1]) {
                // Inv && (r' - l' > 1) && (l' < m' < r') && (a[m'] > a[m' - 1])
                // (R <= a[l']) && (R <= a[m']) && (l' <= index(R) < m')
                r = m;
                // (R <= a[l']) && (R <= a[r']) && (l' <= index(R) < r')
            }
            else {
                // Inv && (r' - l' > 1) && (l' < m' < r') && (a[m'] <= a[m' - 1])
                // (R <= a[m']) && (R <= a[r']) && (m' <= index(R) < r')
                l = m;
                // (R <= a[l']) && (R <= a[r']) && (l' <= index(R) < r')
            }
            // Inv
        }
        // Inv && (r' - l' <= 1)
        // (R <= a[l']) && (l' + 1 == a.length || R <= a[l' + 1]) && (l' <= index(R) < l' + 1)
        // index(R) = l'
        // R = a[l']
        return a[l];
    }

    // :NOTE: Есть подозрение что l в предусловии и аргумент l не имеют ничего общего (исправлено)
    /* Pred: The only one f: 0 <= i < j <= f: => arr[i] > arr[j] && 
                            arr[f] <= arr[f+1] && (f + 1) <= ii < jj < arr.lenght: => arr[ii] < arr[jj]
    */
    // :NOTE: Неформально (исправлено)
    // Post: for (int i = 0; i < arr.length; i++) R <= arr[i]
    public static int recursiveSearch(int[] a, int l, int r) {
        // Inv: (R <= a[l']) && (r' == a.length || R <= a[r']) && (l' <= index(R) < r')
        if (r - l <= 1) {
            // Inv && (r' - l' <= 1)
            // (R <= a[l']) && (l' + 1 == a.length || R <= a[l' + 1]) && (l' <= index(R) < l' + 1)
            // index(R) = l'
            // R = a[l']               
            return a[l];
        }
        // Inv && (r' - l' > 1)
        // Inv && (r' - l' > 1) && (l' < (l' + r')/2 < r')
        int m = (l + r)/2;
        // Inv && (r' - l' > 1) && (l' < m' < r')
        if (a[m] > a[m - 1]) {
            // Inv && (r' - l' > 1) && (l' < m' < r') && (a[m'] > a[m' - 1])
            // (R <= a[l']) && (R <= a[m']) && (l' <= index(R) < m')
            return recursiveSearch(a, l, m);
        }
        else {
            // Inv && (r' - l' > 1) && (l' < m' < r') && (a[m'] <= a[m' - 1])
            // (R <= a[m']) && (R <= a[r']) && (m' <= index(R) < r')
            return recursiveSearch(a, m, r);
        }
    }

    /* Pred: The only one f: 0 <= i < j <= f: => arr[i] > arr[j] && 
                            arr[f] <= arr[f+1] && (f + 1) <= ii < jj < arr.lenght: => arr[ii] < arr[jj]
    */
    public static void main(String[] args){
        int[] a = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        System.out.println(recursiveSearch(a, 0, a.length));
        //System.out.println(iterativeSearch(a));
    }
}

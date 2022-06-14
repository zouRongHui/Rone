package org.rone.core.algorithm;

import java.util.*;

/**
 * 排序算法
 * 专业术语解释
 *  稳定性：两个相同的元素最后的排序仍然保持原来的排序规则
 * @author rone
 */
public class Sort {

    public static void main(String[] args) {
        System.out.println("冒泡排序:");
        int[] bubblingSortArray = new int[]{5,8,6,3,9,2,1,7};
        System.out.println("    原数组：" + Arrays.toString(bubblingSortArray));
        bubblingSort(bubblingSortArray);
        System.out.println("    新数组：" + Arrays.toString(bubblingSortArray));

        System.out.println("冒泡排序的优化1:");
        int[] bubblingSort2Array = new int[]{5,8,6,3,9,2,1,7};
        System.out.println("    原数组：" + Arrays.toString(bubblingSort2Array));
        bubblingSort2(bubblingSort2Array);
        System.out.println("    新数组：" + Arrays.toString(bubblingSort2Array));

        System.out.println("冒泡排序的优化2:");
        int[] bubblingSort3Array = new int[]{5,8,6,3,9,2,1,7};
        System.out.println("    原数组：" + Arrays.toString(bubblingSort3Array));
        bubblingSort3(bubblingSort3Array);
        System.out.println("    新数组：" + Arrays.toString(bubblingSort3Array));

        System.out.println("鸡尾酒排序:");
        int[] cocktailArray = new int[]{2,3,4,5,6,7,8,1};
        System.out.println("    原数组：" + Arrays.toString(cocktailArray));
        cocktail(cocktailArray);
        System.out.println("    新数组：" + Arrays.toString(cocktailArray));

        System.out.println("快速排序 - 双边循环法实现:");
        int[] quickSortArray = new int[] {4,4,6,5,3,2,8,1};
        System.out.println("    原数组：" + Arrays.toString(quickSortArray));
        quickSort(quickSortArray, 0, quickSortArray.length-1);
        System.out.println("    新数组：" + Arrays.toString(quickSortArray));

        System.out.println("快速排序 - 单边循环法实现:");
        int[] quickSort2Array = new int[] {4,4,6,5,3,2,8,1};
        System.out.println("    原数组：" + Arrays.toString(quickSort2Array));
        quickSort2(quickSort2Array, 0, quickSort2Array.length-1);
        System.out.println("    新数组：" + Arrays.toString(quickSort2Array));

        System.out.println("堆排序:");
        int[] heapSortArray = new int[] {1,3,2,6,5,7,8,9,10,0};
        System.out.println("    原数组：" + Arrays.toString(heapSortArray));
        heapSort(heapSortArray);
        System.out.println("    新数组：" + Arrays.toString(heapSortArray));

        System.out.println("计数排序:");
        int[] countSortArray = new int[] {95,94,91,98,99,90,99,93,91,92};
        System.out.println("    原数组：" + Arrays.toString(countSortArray));
        countSort(countSortArray);
        System.out.println("    新数组：" + Arrays.toString(countSortArray));

        System.out.println("桶排序:");
        double[] bucketSortArray = new double[]{4.12,6.421,0.0023,3.0,2.123,8.122,4.12, 10.09};
        System.out.println("    原数组：" + Arrays.toString(bucketSortArray));
        bucketSort(bucketSortArray);
        System.out.println("    新数组：" + Arrays.toString(bucketSortArray));

        System.out.println("选择排序:");
        int[] chooseSortArray = new int[]{5,8,6,3,9,2,1,7};
        System.out.println("    原数组：" + Arrays.toString(chooseSortArray));
        chooseSort(chooseSortArray);
        System.out.println("    新数组：" + Arrays.toString(chooseSortArray));

        System.out.println("插入排序:");
        int[] insertSortArray = new int[]{5,8,6,3,9,2,1,7};
        System.out.println("    原数组：" + Arrays.toString(insertSortArray));
        insertSort(insertSortArray);
        System.out.println("    新数组：" + Arrays.toString(insertSortArray));

        System.out.println("希尔排序:");
        int[] shellSortArray = new int[]{5,8,6,3,9,2,1,7};
        System.out.println("    原数组：" + Arrays.toString(shellSortArray));
        shellSort(shellSortArray);
        System.out.println("    新数组：" + Arrays.toString(shellSortArray));

        System.out.println("基数排序:");
        int[] radixSortArray = new int[]{50, 123, 543, 187, 49, 30, 0, 2, 11, 100};
        System.out.println("    原数组：" + Arrays.toString(radixSortArray));
        radixSort(radixSortArray);
        System.out.println("    新数组：" + Arrays.toString(radixSortArray));

        System.out.println("归并排序:");
        int[] mergerSortArray = new int[]{50, 123, 543, 187, 49, 30, 0, 2, 11, 100};
        System.out.println("    原数组：" + Arrays.toString(mergerSortArray));
        mergerSort(mergerSortArray);
        System.out.println("    新数组：" + Arrays.toString(mergerSortArray));
    }

    /**
     * 冒泡排序，依次比较自己和右边的数，将较大的数置于右边。
     * 最好时间复杂度 O(n)
     * 时间复杂度 O(n~2)
     * 空间复杂度 O(1)
     * 稳定排序
     */
    public static void bubblingSort(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length - i -1; j++) {
                int temp;
                if (nums[j] > nums[j + 1]) {
                    temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
    }

    /**
     * 冒泡排序的优化
     * 	在检测到数组已经有序后不在进行下一次循环检测。
     */
    public static void bubblingSort2(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            //有序标记，每一轮的初始值都是true
            boolean isSorted = true;
            for (int j = 0; j < array.length - i - 1; j++) {
                int tmp;
                if (array[j] > array[j + 1]) {
                    tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                    //因为有元素进行交换，所以不是有序的，标记变为false
                    isSorted = false;
                }
            }
            if (isSorted) {
                break;
            }
        }
    }

    /**
     * 冒泡排序的优化
     * 	因为冒泡排序会见较大的排到右侧，后续的检测的时候后面其实已经不用检测了。
     */
    public static void bubblingSort3(int[] array) {
        //记录最后一次交换的位置
        int lastExchangeIndex = 0;
        //无序数列的边界，每次比较只需要比到这里为止
        int sortBorder = array.length - 1;
        for (int i = 0; i < array.length - 1; i++) {
            //有序标记，每一轮的初始值都是true
            boolean isSorted = true;
            for (int j = 0; j < sortBorder; j++) {
                int tmp;
                if (array[j] > array[j + 1]) {
                    tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                    // 因为有元素进行交换，所以不是有序的，标记变为false
                    isSorted = false;
                    // 更新为最后一次交换元素的位置
                    lastExchangeIndex = j;
                }
            }
            sortBorder = lastExchangeIndex;
            if (isSorted) {
                break;
            }
        }
    }

    /**
     * 鸡尾酒排序 - 冒泡排序的变种
     * 	标准冒泡排序为单向比较排序而鸡尾酒排序为双向排序比较(类似一个摆钟)。
     * 	比较好解决了大部分已经有序的数组的排序。
     * 时间复杂度 O(n~2)
     * 空间复杂度 O(1)
     * 稳定排序
     */
    public static void cocktail(int[] array) {
        int tmp;
        for (int i = 0; i < array.length / 2; i++) {
            //有序标记，每一轮的初始值都是true
            boolean isSorted = true;
            //奇数轮，从左向右比较和交换
            for (int j = i; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    tmp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = tmp;
                    // 有元素交换，所以不是有序的，标记变为false
                    isSorted = false;
                }
            }
            if (isSorted) {
                break;
            }
            //在偶数轮之前，将isSorted重新标记为true
            isSorted = true;
            //偶数轮，从右向左比较和交换
            for (int j = array.length - i - 1; j > i; j--) {
                if (array[j] < array[j - 1]) {
                    tmp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = tmp;
                    // 因为有元素进行交换，所以不是有序的，标记变为false
                    isSorted = false;
                }
            }
            if (isSorted) {
                break;
            }
        }
    }

    /**
     * 快速排序 - 双边循环法实现
     *  每一轮挑选一个基准元素，并让其他比它大的元素移动到数列一边，比它小的元素移动到数列的另一边，从而把数列拆解成两个部分。
     *  拆分开的两个小数组再次进行快速排序，直到数组无法拆分。
     * 双边循环法
     *  通过left、right两个指针从左右同时循环，在相遇之前将数组拆分为大于和小于基准元素的两部分，相遇点即为基准元素最终所在位置。
     * 平均时间复杂度 O(nlogn)
     * 最坏时间复杂度 O(n^2)
     * 空间复杂度 O(logn)
     * 不稳定排序
     */
    public static void quickSort(int[] arr, int startIndex, int endIndex) {
        // 递归结束条件：startIndex大于或等于endIndex时
        if (startIndex >= endIndex) {
            return;
        }
        // 得到基准元素位置
        // 取第1个位置（也可以选择随机位置）的元素作为基准元素
        int pivot = arr[startIndex];
        int left = startIndex;
        int right = endIndex;
        int temp;
        while (left != right) {
            //控制right 指针比较并左移
            while (left < right && arr[right] > pivot) {
                right--;
            }
            //控制left指针比较并右移
            while (left < right && arr[left] <= pivot) {
                left++;
            }
            //交换left和right 指针所指向的元素
            if (left < right) {
                temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }
        //pivot 和指针重合点交换
        arr[startIndex] = arr[left];
        arr[left] = pivot;
        int pivotIndex = left;

        // 根据基准元素，分成两部分进行递归排序
        quickSort(arr, startIndex, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, endIndex);
    }

    /**
     * 快速排序 - 单边循环法实现
     * 单边循环法
     * 	通过一个mark指针来表明基准元素最终所在边界。
     */
    public static void quickSort2(int[] arr, int startIndex, int endIndex) {
        // 递归结束条件：startIndex大于或等于endIndex时
        if (startIndex >= endIndex) {
            return;
        }
        // 得到基准元素位置
        // 取第1个位置（也可以选择随机位置）的元素作为基准元素
        int pivot = arr[startIndex];
        int mark = startIndex;
        for (int i = startIndex + 1; i <= endIndex; i++) {
            if (arr[i] < pivot) {
                mark++;
                int p = arr[mark];
                arr[mark] = arr[i];
                arr[i] = p;
            }
        }

        arr[startIndex] = arr[mark];
        arr[mark] = pivot;
        int pivotIndex = mark;
        // 根据基准元素，分成两部分进行递归排序
        quickSort2(arr, startIndex, pivotIndex - 1);
        quickSort2(arr, pivotIndex + 1, endIndex);
    }

    /**
     * 堆排序，利用二叉堆最大、最小堆来实现。
     * 实现步骤，二叉堆底层使用数组存储数据。
     * 	现将无序数组构建成二叉堆，需要从小到大排序，则构建成最大堆；需要从大到小排序，则构建成最小堆。
     * 	循环删除堆顶元素，替换到二叉堆的末尾，调整堆产生新的堆顶。
     * 	最终得到的数组即为有序的数组。
     * 最好时间复杂度 O(n)
     * 平均时间复杂度 O(nlogn)
     * 最坏时间复杂度 O(n^2)
     * 空间复杂度 O(1)
     * 不稳定排序
     * @param array 待调整的堆
     */
    public static void heapSort(int[] array) {
        // 1. 把无序数组构建成最大堆
        for (int i = (array.length - 2) / 2; i >= 0; i--) {
            downAdjust(array, i, array.length);
        }
        // 2. 循环删除堆顶元素，移到集合尾部，调整堆产生新的堆顶
        int temp;
        for (int i = array.length - 1; i > 0; i--) {
            // 最后1个元素和第1个元素进行交换
            temp = array[i];
            array[i] = array[0];
            array[0] = temp;
            // “下沉”调整最大堆
            downAdjust(array, 0, i);
        }
    }

    /**
     * “下沉”调整
     * @param array       待调整的堆
     * @param parentIndex 要“下沉”的父节点
     * @param length      堆的有效大小
     */
    private static void downAdjust(int[] array, int parentIndex, int length) {
        // temp 保存父节点值，用于最后的赋值
        int temp = array[parentIndex];
        int childIndex = 2 * parentIndex + 1;
        while (childIndex < length) {
            // 如果有右孩子，且右孩子大于左孩子的值，则定位到右孩子
            if (childIndex + 1 < length && array[childIndex + 1] > array[childIndex]) {
                childIndex++;
            }
            // 如果父节点大于任何一个孩子的值，则直接跳出
            if (temp >= array[childIndex]) {
                break;
            }
            //无须真正交换，单向赋值即可
            array[parentIndex] = array[childIndex];
            parentIndex = childIndex;
            childIndex = 2 * childIndex + 1;
        }
        array[parentIndex] = temp;
    }

    /**
     * 计数排序
     * 	通过建立大小为范围值得数组，已范围值数组下标作为元素值，范围值元素大小为元素出现的次数。
     * 	遍历    原数组，填充范围值数组，最后将范围值数组按照元素值数量依次输出。
     * 优化，基础版本无法做到稳定性。代码基于此实现
     * 	范围值数组元素值由原来表示元素次数改为表示最后一个该元素在最终有序数组中的位置。
     * 	在原有范围值数组基础上将元素值改为该下标之前的所有元素的值的和(包含自己)，这样就将元素值的含义改为最后一个该元素在最终有序数组中的位置。
     * 	遍历输出数组的时候倒序遍历，在获取一个元素后将其在范围值对应下标的元素值减一，表示下一个该元素的最终位置。
     * 缺点：
     * 	当最小值和最大值差距过大且元素分布不均匀时，例如20个元素范围在1-1亿之间。资源消耗太大。
     * 	当元素不是整数的时候。但该缺点可以通过将元素和整数整理一个对应关系来处理，例如赤橙黄绿青蓝紫对应1-7。
     * 时间复杂度 O(n+m)，m表示范围值
     * 空间复杂度 O(m+n)
     * 稳定排序
     */
    public static void countSort(int[] array) {
        //1.得到数列的最大值和最小值，并算出差值d
        int max = array[0];
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
            if (array[i] > max) {
                max = array[i];
            }
        }
        int d = max - min;
        //2.创建统计数组并统计对应元素的个数
        int[] countArray = new int[d + 1];
        for (int j : array) {
            countArray[j - min]++;
        }
        //3.统计数组做变形，后面的元素等于前面的元素之和
        for (int i = 1; i < countArray.length; i++) {
            countArray[i] += countArray[i - 1];
        }
        //4.倒序遍历原始数列，从统计数组找到正确位置，输出到结果数组
        int[] sortedArray = new int[array.length];
        for (int i = array.length - 1; i >= 0; i--) {
            sortedArray[countArray[array[i] - min] - 1] = array[i];
            countArray[array[i] - min]--;
        }
        System.arraycopy(sortedArray, 0, array, 0, sortedArray.length);
    }

    /**
     * 桶排序，创建若干个桶来协助排序。每一个桶（bucket）代表一个区间范围，里面可以承载一个或多个元素。
     * 平均时间复杂度 O(n+m)，m为拆分的桶的数量
     * 最坏时间复杂度 O(n^2)
     * 空间复杂度 O(n+m)
     * 稳定排序
     */
    public static void bucketSort(double[] array) {
        //1.得到数列的最大值和最小值，并算出差值d
        double max = array[0];
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
        double d = max - min;
        //2.初始化桶
        int bucketNum = array.length;
        ArrayList<LinkedList<Double>> bucketList = new ArrayList<>(bucketNum);
        for (int i = 0; i < bucketNum; i++) {
            bucketList.add(new LinkedList<>());
        }
        //3.遍历原始数组，将每个元素放入桶中
        for (double v : array) {
            int num = (int) ((v - min) * (bucketNum - 1) / d);
            bucketList.get(num).add(v);
        }
        //4.对每个桶内部进行排序
        for (LinkedList<Double> doubles : bucketList) {
            //JDK 底层采用了归并排序或归并的优化版本
            Collections.sort(doubles);
        }
        //5.输出全部元素
        double[] sortedArray = new double[array.length];
        int index = 0;
        for (LinkedList<Double> list : bucketList) {
            for (double element : list) {
                sortedArray[index] = element;
                index++;
            }
        }
        System.arraycopy(sortedArray, 0, array, 0, sortedArray.length);
    }

    /**
     * 选择排序，就是从第一个数开始，与后面所有的数相比较，找出最小的数，放在第一个位置，以此类推，每一轮确定一个相对于这一轮最小的数。
     * 最好时间复杂度 O(1)
     * 时间复杂度 O(n^2)
     * 空间复杂度 O(1)
     * 不稳定排序
     */
    public static void chooseSort(int[] nums) {
        for (int i = 0; i < nums.length-1; i++) {
            int minIndex = i;
            for (int j = i+1; j < nums.length; j++) {
                if (nums[minIndex] > nums[j]) {
                    minIndex = j;
                }
            }
            if (nums[i] > nums[minIndex]) {
                int n = nums[i];
                nums[i] = nums[minIndex];
                nums[minIndex] = n;
            }
        }
    }

    /**
     * 插入排序，选中一个数，他的左边已经有序(已实现),依次和左边项去比较排序
     * 时间复杂度 O(n~2)
     * 空间复杂度 O(1)
     * 稳定排序
     */
    public static void insertSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            for (int j = i; j > 0; j--) {
                if (nums[j] > nums[j-1]) {
                    break;
                }
                int n = nums[j];
                nums[j] = nums[j-1];
                nums[j-1] = n;
            }
        }
    }

    /**
     * 希尔排序，插入排序的改进版，优先比较距离较远的元素，又叫缩小增量排序
     * 最好时间复杂度 O(n)
     * 平均时间复杂度 O(n^2)
     * 空间复杂度 O(1)
     * 不稳定排序
     */
    public static void shellSort(int[] nums) {
        int gap = 1;
        double three = 3.0;
        while (gap < nums.length/three) {
            gap = gap*3 + 1;
        }
        for (; gap > 0; gap = (int) Math.floor(gap/three)) {
            for (int i = gap; i < nums.length; i++) {
                int temp = nums[i];
                for (int j = i-gap; (j>=0) && (nums[j]>temp); j -= gap) {
                    nums[j+gap] = nums[j];
                    nums[j] = temp;
                }
            }
        }
    }

    /**
     * 基数排序
     *  将要排序的元素拆分不同的基数元素，依次排序，最终达到整体排序的效果。
     *  例如下面的示例，整数排序，先排序其个位，在排序其百位、千位...，最终实现整体排序。
     * 平均时间复杂度 O(n*m)，m为基数拆分后的次数
     * 空间复杂度 O(n+m)
     * 稳定排序
     */
    public static void radixSort(int[] nums) {
        // 基数元素数组
        int[] radixObjects = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        // 基数拆分后的次数
        int radixNum = 3;
        // 基数获取规则
        int[] radixRules = new int[]{1, 10, 100};
        // 记录基数元素桶中数据个数
        Map<Integer, Integer> bucketCountMap = new HashMap<>(radixObjects.length);
        // 缓存数据的数组
        int[] bucket = new int[nums.length];
        // 从个位开始排序
        for (int i = 0; i < radixNum; i++) {
            // 置空各个桶的数据统计
            for (int radixObject : radixObjects) {
                bucketCountMap.put(radixObject, 0);
            }
            // 统计各个桶将要装入的数据个数
            for (int num : nums) {
                // 获取基数元素
                int radix = ((num / radixRules[i]) % 10);
                bucketCountMap.put(radix, bucketCountMap.get(radix) + 1);
            }
            // 统计桶的右边界
            for (int j = 1; j < radixObjects.length; j++) {
                bucketCountMap.put(j, bucketCountMap.get(j) + bucketCountMap.get(j - 1));
            }
            // 将数据依次装入桶中，这里要从右向左扫描，保证排序稳定性
            for (int j = (nums.length - 1); j >= 0; j--) {
                int radix = ((nums[j] / radixRules[i]) % 10);
                bucket[bucketCountMap.get(radix) - 1] = nums[j];
                bucketCountMap.put(radix, bucketCountMap.get(radix) - 1);
            }
            // 将已分配好的桶中数据再倒出来，此时已是对应当前基数有序的表
            System.arraycopy(bucket, 0, nums, 0, bucket.length);
        }
    }

    /**
     * 归并排序
     * 分割：把未排序的列表划分为 n 个子列表，每个包含一个元素（只有一个元素的列表被认为是有序的）。
     * 合并：不停地合并子列表生成新的已排序列表，直到最后合并为一个已排序的列表。
     * @param nums
     */
    public static void mergerSort(int[] nums) {
        int length = nums.length;
        // 将原数组拆分为n个有序的子数组(单个元素的数组总是有序的)
        int[][] temp = new int[nums.length][1];
        for (int i = 0; i < nums.length; i++) {
            temp[i][0] = nums[i];
        }
        // 两两按序合并
        int[] a1 = temp[0];
        for (int i = 1; i < temp.length; i++) {
            int[] a2 = temp[i];
            a1 = merger(a1, a2);
        }
        System.arraycopy(a1, 0, nums, 0, a1.length);
    }

    /**
     * 按序合并
     * @param a1
     * @param a2
     * @return
     */
    private static int[] merger(int[] a1, int a2[]) {
        //left控制a1数组的下标
        int left = 0;
        //right控制a2数组的下标
        int right = 0;
        //x控制新数组的下标
        int x = 0;
        //定义一个新数组arr
        int[] arr = new int[a1.length + a2.length];
        while (left < a1.length && right < a2.length) {
            //如果left指向的数小于right指向的数
            //就把a1[left]写入到新数组arr中
            if (a1[left] < a2[right]) {
                arr[x++] = a1[left++];
            }
            //如果right指向的数小于left指向的数
            //就把arr[right]写入到新数组arr中
            else {
                arr[x++] = a2[right++];
            }
        }
        //比较完后，若a1数组有剩余，将剩余部分写入新数组的后面
        while (left < a1.length) {
            arr[x++] = a1[left++];
        }
        //比较完后，若a2数组有剩余，将剩余部分写入新数组的后面
        while (right < a2.length) {
            arr[x++] = a2[right++];
        }
        //返回数组
        return arr;
    }
}

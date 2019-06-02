package lam.spark.sample;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.Arrays;
import java.util.Objects;


/**
 * @description Word2VecSample
 * @author linanmiao
 * @date 2019/6/2
 * @version 1.0
 */
public class RecommendMovieSample implements Closeable, Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendMovieSample.class);

    private static final String PARENT_PATH = "D:/document/Spark/ml-100k";

    private final SparkSession sparkSession;

    public RecommendMovieSample() {
        this.sparkSession = this.initSparkSession();
    }

    private SparkSession initSparkSession() {
        final SparkConf sparkConf = new SparkConf()
                .setAppName(RecommendMovieSample.class.getSimpleName())
                .setMaster("local[*]");
        return SparkSession.builder()
                .config(sparkConf)
                .getOrCreate();
    }

    @Override
    public void run() {
        Objects.requireNonNull(this.sparkSession, "SparkSession can not be null!");
        final JavaSparkContext sparkContext = new JavaSparkContext(this.sparkSession.sparkContext());

        // 提取特征 ========================

        // 生成用户评分数据的RDD，格式为：用户 电影 评分 时间戳
        // 例子：196	242	3	881250949
        final JavaRDD<String> javaRDD = sparkContext.textFile(PARENT_PATH + "/u.data");

        /* 去掉时间戳的字段，格式变为：用户 电影 评分；*/
        /* 格式变为：Rating(用户 电影 评分)，作为后续训练模型的参数 */
        final JavaRDD<Rating> ratingRDD = javaRDD.map(s -> {
            final String[] strings = s.split("\\t");
            return new Rating(
            Integer.parseInt(strings[0]),  // userId
            Integer.parseInt(strings[1]), // movieId
            Float.parseFloat(strings[2])  // rating 评分
            );
        });

        LOGGER.info("Rating first: {}", ratingRDD.first());

        // 训练模型 ========================
        final ALS als = new ALS();
        // 最小二乘法的模型需要以下三个参数
        /*
         * rank
         * 对应ALS模型中的因子个数，也就是在低阶近似矩阵中的隐含特征个数。
         * 因子个数一般越多越好。但它也会接影响模型训练和保存时所需的内存开销，尤其是在用户和物品很多的时候。
         * 因此实践中该参数常作为训练效果与系统开销之间的调节参数。通常，其合理取值为10到200。
         */
        final int rank = 50;
        /*
         * iterations
         * 对应运行时的迭代次数。
         * ALS能确保每次迭代都能降低评级矩阵的重建误差，但一般经少数次迭代后ALS模型便已能收敛为一个比较合理的好模型。
         * 这样，大部分情况下都没必要迭代太多次（10次左右一般就挺好）。
         */
        final int iterations = 10;
        /*
         * lambda
         * 该参数控制模型的正则化过程，从而控制模型的过拟合情况。
         * 其值越高，正则化越严厉。该参数的赋值与实际数据的大小、特征和稀疏程度有关。
         * 和其他的机器学习模型一样，正则参数应该通过用非样本的测试数据进行交叉验证来调整。
         */
        final double lambda = 0.01D;

        // 得到了推荐的模型
        final MatrixFactorizationModel model = ALS.train(ratingRDD.rdd(), rank, iterations, lambda);

        // 使用模型 ========================
        // 1.用户推荐
        //为指定的用户推荐 N 个商品
        final int userId = 789;
        final int topN = 10;
        final Rating[] topNRating = model.recommendProducts(userId, topN);
        LOGGER.info("topN: {}", Arrays.toString(topNRating));
    }

    @Override
    public void close() {
        if (this.sparkSession != null) {
            try {
                this.sparkSession.close();
            } catch (final Exception e) {
                LOGGER.error("close SparkSession failed", e);
            }
        }
    }

    public static void main(final String[] args) {
        final RecommendMovieSample sample = new RecommendMovieSample();
        sample.run();
        sample.close();
    }
}

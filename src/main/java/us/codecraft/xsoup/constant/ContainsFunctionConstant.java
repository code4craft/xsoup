package us.codecraft.xsoup.constant;

import org.jsoup.select.Evaluator;

/**
 * @author liu xw
 * @date 2023 07-10
 */
public class ContainsFunctionConstant {

    /**
     * 包含文本信息 - {@link Evaluator.ContainsOwnText}
     * 匹配直接子级文本内容
     */
    public static final String TEXT_ONE = ":text:one";

    /**
     * 包含整个文本信息 - {@link Evaluator.ContainsText}
     * 匹配所有文本内容
     */
    public static final String TEXT_ALL = ":text:all";

    /**
     * 包含属性信息 - {@link Evaluator.ContainsData}
     * 匹配所有文本内容
     */
    public static final String DATA_ALL = ":data:all";

    /**
     * 属性 - 不包含 - {@link Evaluator.AttributeWithValue}
     */
    public static final String ATTRIBUTE = ":@";

    /**
     * 属性 - 不包含 - {@link Evaluator.AttributeWithValueNot}
     */
    public static final String ATTRIBUTE_NOT = ":not:@";

    /**
     * 属性 - xx开始 - {@link Evaluator.AttributeWithValueStarting}
     */
    public static final String ATTRIBUTE_START = ":start:@";
}

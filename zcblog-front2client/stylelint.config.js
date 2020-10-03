// 开启stylelint检测css、stylus语法
module.exports = {
  ignoreFiles: ['**/*.js'],
  extends: ['stylelint-config-standard', 'stylelint-plugin-stylus/recommended'],
  rules: {
    // override/add rules settings here, such as:
    // "stylus/declaration-colon": "never"
    'no-empty-source': null, // 允许空源
    'font-family-no-duplicate-names': null, // 允许使用重复的字体名称
    'value-list-comma-space-after': 'always', // 在值列表的逗号之后要求有一个空格
    'selector-pseudo-element-colon-notation': 'single', // 指定伪元素使用双冒号
    'declaration-colon-space-before': 'never', // 在冒号之前禁止有空白
    'declaration-colon-space-after': 'always-single-line', // 在多行值列表的冒号之后必须有一个换行符
    'color-hex-length': 'long', // 指定十六进制颜色不使用缩写
    'value-keyword-case': 'lower', // 指定关键字的值采用小写
    'media-feature-name-no-unknown': null, // 允许使用未知的media特性名称
    'media-query-list-comma-newline-before': 'never-multi-line', // 在多行媒体查询列表的逗号之前禁止有空白
    'media-query-list-comma-space-after': 'always', // 在媒体查询的逗号之后要求有一个空格
    'rule-empty-line-before': null, // 在规则之前并非必须有一空行
    'font-family-no-missing-generic-family-keyword': null
  }
}

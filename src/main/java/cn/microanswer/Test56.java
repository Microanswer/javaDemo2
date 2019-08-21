package cn.microanswer;


import java.io.File;

public class Test56 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2019年08月21日 17:07:58
     */
    public static void main(String[] args) throws Exception {

        String[] links = {
                "https://i0.hdslb.com/bfs/archive/bf3ea8541e1bd469255a907f7b60015ad9abf1ce.png 微笑",
                "https://i0.hdslb.com/bfs/archive/de4c0783aaa60ec03de0a2b90858927bfad7154b.png 嫌弃",
                "https://i0.hdslb.com/bfs/archive/8a10a4d73a89f665feff3d46ca56e83dc68f9eb8.png 喜欢",
                "https://i0.hdslb.com/bfs/archive/33ad6000d9f9f168a0976bc60937786f239e5d8c.png 呆",
                "https://i0.hdslb.com/bfs/archive/2caafee2e5db4db72104650d87810cc2c123fc86.png 大哭",
                "https://i0.hdslb.com/bfs/archive/9d2ec4e1fbd6cb1b4d12d2bbbdd124ccb83ddfda.png 害羞",
                "https://i0.hdslb.com/bfs/archive/44667b7d9349957e903b1b62cb91fb9b13720f04.png 无语",
                "https://i0.hdslb.com/bfs/archive/d2f26cbdd6c96960320af03f5514c5b524990840.png 委屈",
                "https://i0.hdslb.com/bfs/archive/cb321684ed5ce6eacdc2699092ab8fe7679e4fda.png 尴尬",
                "https://i0.hdslb.com/bfs/archive/3195714219c4b582a4fb02033dd1519913d0246d.png 生气",
                "https://i0.hdslb.com/bfs/archive/bb84906573472f0a84cebad1e9000eb6164a6f5a.png 奸笑",
                "https://i0.hdslb.com/bfs/archive/ca94ad1c7e6dac895eb5b33b7836b634c614d1c0.png 大笑",
                "https://i0.hdslb.com/bfs/archive/f8e9a59cad52ae1a19622805696a35f0a0d853f3.png 惊讶",
                "https://i0.hdslb.com/bfs/archive/12e41d357a9807cc80ef1e1ed258127fcc791424.png 囧",
                "https://i0.hdslb.com/bfs/archive/6921bb43f0c634870b92f4a8ad41dada94a5296d.png 捂脸",
                "https://i0.hdslb.com/bfs/archive/43d3db7d97343c01b47e22cfabeca84b4251f35a.png 灵魂出窍",
                "https://i0.hdslb.com/bfs/archive/06946bfe71ac48a6078a0b662181bb5cad09decc.png 吐",
                "https://i0.hdslb.com/bfs/archive/cb89184c97e3f6d50acfd7961c313ce50360d70f.png 抠鼻",
                "https://i0.hdslb.com/bfs/archive/0afecaf3a3499479af946f29749e1a6c285b6f65.png 惊喜",
                "https://i0.hdslb.com/bfs/archive/010540d0f61220a0db4922e4a679a1d8eca94f4e.png 傲娇",
                "https://i0.hdslb.com/bfs/archive/905fd9a99ec316e353b9bd4ecd49a5f0a301eabf.png 疼",
                "https://i0.hdslb.com/bfs/archive/ba8d5f8e7d136d59aab52c40fd3b8a43419eb03c.png 阴险",
                "https://i0.hdslb.com/bfs/archive/ba4de7a3f97644038b15195bdc9f82a8fd118e77.png 画风突变",
                "https://i0.hdslb.com/bfs/archive/b4cb77159d58614a9b787b91b1cd22a81f383535.png 妙啊",
                "https://i0.hdslb.com/bfs/archive/bba7c12aa51fed0199c241465560dfc2714c593e.png doge",
                "https://i0.hdslb.com/bfs/archive/d15121545a99ac46774f1f4465b895fe2d1411c3.png 滑稽",
                "https://i0.hdslb.com/bfs/archive/4191ce3c44c2b3df8fd97c33f85d3ab15f4f3c84.png 吃瓜",
                "https://i0.hdslb.com/bfs/archive/3c210366a5585706c09d4c686a9d942b39feeb50.png 支持",
                "https://i0.hdslb.com/bfs/archive/9c10c5ebc7bef27ec641b8a1877674e0c65fea5d.png 吓",
                "https://i0.hdslb.com/bfs/archive/1a67265993913f4c35d15a6028a30724e83e7d35.png 点赞",
                "https://i0.hdslb.com/bfs/archive/e64af664d20716e090f10411496998095f62f844.png 嘘声",
                "https://i0.hdslb.com/bfs/archive/0f25ce04ae1d7baf98650986454c634f6612cb76.png 生病",
                "https://i0.hdslb.com/bfs/archive/1b5c53cf14336903e1d2ae3527ca380a1256a077.png 响指",
                "https://i0.hdslb.com/bfs/archive/e90ec4c799010f25391179118ccd9f66b3b279ba.png 黑洞",
                "https://i0.hdslb.com/bfs/archive/177999fb7d70d891fbf63b161f26b272e08dc1de.png 粽子",
        };

        File dir = new File("C:\\Users\\Microanswer\\File\\其他\\(图)表情\\哔哩哔哩表情");
        for (String s : links) {
            String[] ls = s.split(" ");
            String link = ls[0];
            String name = ls[1];
            File download = HttpUtil.download(link, dir);
            boolean b = download.renameTo(new File(dir, name + ".png"));
            System.out.println(b ? "success。" : "失败：" + name);
        }

        
    }
}

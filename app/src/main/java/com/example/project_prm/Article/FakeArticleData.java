package com.example.project_prm.Article;

import java.util.ArrayList;
import java.util.List;

public class FakeArticleData {

    public static List<Article> getSampleArticles() {
        List<Article> articles = new ArrayList<>();

        articles.add(new Article(1,
                "Tác động dài hạn của COVID-19 đến sức khỏe cộng đồng",
                "Dù đại dịch COVID-19 đã lắng xuống tại nhiều quốc gia, nhưng hậu quả mà nó để lại vẫn còn kéo dài. Các chuyên gia y tế nhận định rằng những người từng mắc COVID-19, đặc biệt là thể nặng, có nguy cơ cao gặp phải các biến chứng về phổi, tim mạch và thần kinh. Một số trường hợp được ghi nhận có biểu hiện mệt mỏi kéo dài, khó thở, mất trí nhớ tạm thời và thậm chí là rối loạn tâm thần. Điều đáng lo ngại là những triệu chứng này không chỉ xuất hiện ở người lớn tuổi mà còn phổ biến ở cả người trẻ tuổi và khỏe mạnh. Ngoài ra, tác động tâm lý của đại dịch cũng khiến tỷ lệ trầm cảm và lo âu gia tăng. Nhiều người mất đi người thân, công việc và bị cô lập xã hội trong thời gian dài đã dẫn đến các vấn đề sức khỏe tinh thần nghiêm trọng. Các nghiên cứu đang tiếp tục để làm rõ mối liên hệ giữa virus SARS-CoV-2 và các biến chứng lâu dài.",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Covid-19",
                "12/07/2024", false, true));

        articles.add(new Article(2,
                "Thói quen ăn uống lành mạnh giúp giảm nguy cơ bệnh tim mạch",
                "Chế độ ăn đóng vai trò then chốt trong việc phòng ngừa và kiểm soát các bệnh tim mạch. Các chuyên gia dinh dưỡng khuyến nghị nên giảm lượng muối, đường và chất béo bão hòa trong khẩu phần ăn hằng ngày. Thay vào đó, người dân nên tăng cường tiêu thụ rau xanh, trái cây, ngũ cốc nguyên hạt, cá và các loại hạt. Một số nghiên cứu còn chỉ ra rằng, việc sử dụng dầu ô liu, ăn cá hồi hoặc cá mòi ít nhất 2 lần mỗi tuần giúp cải thiện sức khỏe tim mạch đáng kể. Bên cạnh đó, việc ăn uống điều độ, đúng giờ, tránh bỏ bữa và hạn chế ăn đêm cũng có tác động tích cực đến huyết áp và lượng cholesterol trong máu.",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Health",
                "10/07/2024", false, false));

        articles.add(new Article(3,
                "Tăng cường vận động thể chất sau giờ làm việc để giảm căng thẳng",
                "Sau một ngày làm việc dài căng thẳng, việc tham gia các hoạt động thể thao nhẹ như đi bộ, đạp xe, tập yoga hay đơn giản là vận động nhẹ nhàng trong nhà có thể giúp cải thiện sức khỏe tinh thần. Theo nghiên cứu của Đại học Stanford, việc duy trì thói quen vận động ít nhất 30 phút mỗi ngày giúp giảm hormone gây stress như cortisol, đồng thời làm tăng endorphin – hormone tạo cảm giác hạnh phúc. Nhiều công ty hiện đã thiết kế không gian tập luyện hoặc khuyến khích nhân viên đi bộ sau giờ làm để nâng cao hiệu quả làm việc và tinh thần tích cực.",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Lifestyle",
                "08/07/2024", false, true));

        articles.add(new Article(4,
                "Vai trò của giấc ngủ trong việc phục hồi và duy trì sức khỏe",
                "Giấc ngủ có vai trò quan trọng trong việc phục hồi chức năng của cơ thể và tinh thần. Thiếu ngủ không chỉ ảnh hưởng đến khả năng tập trung, trí nhớ mà còn làm tăng nguy cơ mắc các bệnh mãn tính như tiểu đường, cao huyết áp và béo phì. Nghiên cứu chỉ ra rằng người trưởng thành cần ít nhất 7-8 giờ ngủ mỗi đêm để duy trì sức khỏe tối ưu. Ngoài ra, việc duy trì lịch trình ngủ đều đặn, tránh sử dụng thiết bị điện tử trước khi ngủ và tạo môi trường ngủ yên tĩnh cũng giúp cải thiện chất lượng giấc ngủ đáng kể.",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Health",
                "06/07/2024", false, false));

        articles.add(new Article(5,
                "Ứng dụng trí tuệ nhân tạo trong chẩn đoán sớm ung thư",
                "Với sự phát triển của công nghệ, trí tuệ nhân tạo (AI) đã được ứng dụng trong ngành y học để hỗ trợ bác sĩ chẩn đoán sớm các bệnh lý nguy hiểm, đặc biệt là ung thư. Các thuật toán học máy có thể phân tích hàng nghìn hình ảnh chụp CT, MRI để phát hiện các bất thường với độ chính xác cao. Một số bệnh viện lớn trên thế giới đã triển khai AI trong sàng lọc ung thư vú, ung thư phổi, tuyến tiền liệt và kết quả ban đầu cho thấy AI giúp rút ngắn thời gian chẩn đoán và tăng tỷ lệ phát hiện bệnh ở giai đoạn đầu – khi cơ hội chữa khỏi cao hơn nhiều.",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Medical",
                "05/07/2024", false, true));

        // Thêm 5 bài tiếp theo tương tự...
        articles.add(new Article(6,
                "Cảnh báo nguy cơ thiếu hụt vitamin D ở người trẻ thành thị",
                "Do môi trường làm việc văn phòng, ít tiếp xúc với ánh nắng, nhiều người trẻ hiện nay đang bị thiếu hụt vitamin D – yếu tố quan trọng cho xương khớp, hệ miễn dịch và tâm trạng. Các bác sĩ khuyến cáo nên bổ sung vitamin D qua thực phẩm giàu dưỡng chất như cá hồi, lòng đỏ trứng, nấm và quan trọng hơn là tắm nắng ít nhất 15 phút mỗi ngày vào buổi sáng. Thiếu vitamin D có thể dẫn đến đau xương, yếu cơ và tăng nguy cơ loãng xương về sau.",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Health",
                "03/07/2024", false, true));

        articles.add(new Article(7,
                "Thói quen uống nước đủ mỗi ngày và lợi ích bất ngờ",
                "Cơ thể con người chứa đến 60-70% là nước, vì vậy việc duy trì lượng nước cần thiết hằng ngày giúp hệ tuần hoàn, tiêu hóa và bài tiết hoạt động hiệu quả. Uống đủ 2 lít nước mỗi ngày giúp da sáng, cải thiện chức năng thận và tăng năng lượng. Tuy nhiên, nhiều người thường xuyên quên uống nước hoặc thay thế bằng nước ngọt có gas, dẫn đến tình trạng mất nước mãn tính. Để cải thiện, bạn nên mang theo bình nước cá nhân, đặt lịch nhắc uống nước và bổ sung trái cây mọng nước như dưa hấu, cam, bưởi...",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Lifestyle",
                "01/07/2024", false, false));

        articles.add(new Article(8,
                "Trầm cảm ở thanh thiếu niên: Nhận diện và can thiệp sớm",
                "Trầm cảm ở tuổi vị thành niên ngày càng phổ biến, nguyên nhân đến từ áp lực học tập, mạng xã hội, kỳ vọng gia đình và khủng hoảng tuổi dậy thì. Dấu hiệu thường gặp gồm buồn bã kéo dài, mất hứng thú với các hoạt động thường ngày, thay đổi thói quen ăn ngủ và có suy nghĩ tiêu cực. Việc nhận diện sớm và can thiệp kịp thời bằng liệu pháp tâm lý, trò chuyện với chuyên gia tâm lý học đường hoặc sự đồng hành từ phụ huynh sẽ giúp trẻ vượt qua khủng hoảng tinh thần này.",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Health",
                "28/06/2024", false, true));

        articles.add(new Article(9,
                "Tác dụng bất ngờ của việc thiền định 15 phút mỗi ngày",
                "Thiền định không chỉ giúp giảm căng thẳng mà còn cải thiện chức năng não bộ, tăng sự tập trung và kiểm soát cảm xúc. Chỉ cần dành 10-15 phút mỗi ngày để ngồi yên tĩnh, điều hòa hơi thở và quan sát suy nghĩ, bạn sẽ cảm thấy bình an hơn. Một số nghiên cứu chỉ ra thiền giúp tăng mật độ chất xám tại vùng liên quan đến trí nhớ, học tập và lòng từ bi. Nhiều người thành công như CEO Google, diễn viên Hollywood đều duy trì thiền như một phần không thể thiếu trong ngày.",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Lifestyle",
                "26/06/2024", false, true));

        articles.add(new Article(10,
                "Nguy cơ kháng kháng sinh do lạm dụng thuốc",
                "Tổ chức Y tế Thế giới cảnh báo việc sử dụng kháng sinh bừa bãi là nguyên nhân chính dẫn đến tình trạng kháng thuốc, khiến nhiều bệnh lý thông thường trở nên khó điều trị hơn. Ở Việt Nam, tình trạng tự ý mua kháng sinh không theo toa, dùng không đủ liều hoặc sử dụng sai bệnh còn phổ biến. Điều này không chỉ gây hại cho người bệnh mà còn làm xuất hiện các chủng vi khuẩn kháng thuốc cực kỳ nguy hiểm. Giải pháp là cần quản lý chặt chẽ kê đơn, tuyên truyền giáo dục cộng đồng và nâng cao năng lực xét nghiệm vi sinh trong các cơ sở y tế.",
                "https://www.amprogress.org/wp-content/uploads/2020/03/Microbes-1-800x450.jpg",
                "Medical",
                "25/06/2024", false, false));

        return articles;
    }
}



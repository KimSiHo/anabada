package me.blog.anabada.common.config;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.common.enums.NotificationStatus;
import me.blog.anabada.common.enums.PostType;
import me.blog.anabada.common.enums.ProductKind;
import me.blog.anabada.controller.form.ProductUploadForm;
import me.blog.anabada.dao.AccountRepository;
import me.blog.anabada.dao.NotificationRepository;
import me.blog.anabada.dao.PostRepository;
import me.blog.anabada.dao.ProductRepository;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Notification;
import me.blog.anabada.entities.Post;
import me.blog.anabada.entities.Product;
import me.blog.anabada.service.impl.ProductService;

//@Profile("!test")
@RequiredArgsConstructor
//@Component
@Order(0)
public class DataInsert implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;

    private final ProductService productService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account account = createAccount("kim125y", "kim125y@naver.com", "12345678");
        /*Post build = Post.builder()
            .content("abc")
            .title("abc")
            .writer(account)
            .postType(PostType.sale)
            .build();
        postRepository.save(build);*/
        /*Account account1 = createAccount("odreamgodo", "odreamgodo@naver.com", "12345678");
        Account account2 = createAccount("kim125", "kim125@naver.com", "12345678");
        Account account3 = createAccount("kim9810", "kim9810@naver.com", "12345678");

        List<Product> products = createProduct();*/

        /*Product buyRequestProduct = products.get(3);
        Product buyRequestProduct2 = products.get(6);
        createNoti(account2, buyRequestProduct.getAccount(), buyRequestProduct, "구매 요청합니다");
        createNoti(account3, buyRequestProduct2.getAccount(), buyRequestProduct2, "구매 요청합니다");

        createPost(account);*/
    }

    private Account createAccount(String nickname, String email, String password) {
        Account account = Account.builder()
            .emailVerified(true)
            .joinedAt(LocalDateTime.now())
            .email(email)
            .nickname(nickname)
            .password(passwordEncoder.encode(password))
            .build();

        return accountRepository.save(account);
    }

    private List<Product> createProduct() throws IOException {
        String root = "src/main/resources/init-data/";
        String[] categories = {"computer", "electronic", "furniture", "cloth", "shoes"};

        List<Product> ret = new ArrayList<>();
        for (String category : categories) {
            String strCategoryDirectory = root + category;
            String strProductDescDirectory = strCategoryDirectory + "/product-description";

            File categoryDirectory = new File(strCategoryDirectory);
            File productDescDirectory = new File(strProductDescDirectory);

            File[] productImgfiles = categoryDirectory.listFiles((parent, fileName) ->
                fileName.endsWith("jpg") || fileName.endsWith("png") || fileName.endsWith("jpeg"));

            ret.addAll(InsertProductData(productDescDirectory, productImgfiles));
        }

        return ret;
    }

    private void createNoti(Account sender, Account recipient, Product audio, String msg) {
        Notification notification = Notification.builder()
            .sender(sender)
            .recipient(recipient)
            .product(audio)
            .message(msg)
            .notificationStatus(NotificationStatus.purchaseRequest)
            .build();

        notificationRepository.save(notification);
    }

    private void createPost(Account account) {
        IntStream.rangeClosed(1, 154).forEach(i -> {
            Post newPost = Post.builder()
                .title("test" + i)
                .content("content test" + i)
                .postType(PostType.sale)
                .writer(account)
                .build();

            postRepository.save(newPost);
        });
    }

    private List<Product> InsertProductData(File productDescDirectory, File[] productImgfiles) throws IOException {

        List<Product> retProducts = new ArrayList<>();

        for (File productImgfile : productImgfiles) {
            String fileName = FilenameUtils.removeExtension(productImgfile.getName());

            // 매칭되는 거 하나 있다고 가정
            File[] files = productDescDirectory.listFiles((parent, txtFileName) -> txtFileName.startsWith(fileName));
            List<String> lines = Files.readAllLines(files[0].toPath());

            ProductUploadForm productUploadForm = new ProductUploadForm();
            Account owner = null;
            for (String line : lines) {
                Integer keyIndex = line.indexOf(">");
                String keyword = line.substring(0, keyIndex).trim();
                String value = StringUtils.trim(line.substring(keyIndex + 1));

                switch (keyword) {
                    case "bio":
                        productUploadForm.setBio(value);
                        break;
                    case "keyword":
                        switch (value) {
                            case "furniture":
                                productUploadForm.setProductKind(ProductKind.furniture);
                                break;
                            case "shoes":
                                productUploadForm.setProductKind(ProductKind.shoes);
                                break;
                            case "cloth":
                                productUploadForm.setProductKind(ProductKind.cloth);
                                break;
                            case "electronic":
                                productUploadForm.setProductKind(ProductKind.electronic);
                                break;
                            case "computer":
                                productUploadForm.setProductKind(ProductKind.computer);
                                break;
                            default:
                                System.out.println("일치하지 않는 키워드 입니다");
                                break;
                        }
                        break;
                    case "owner":
                        owner = accountRepository.findByNickname(value);
                        break;
                    case "productTitle":
                        productUploadForm.setProductTitle(value);
                        break;
                    case "price":
                        productUploadForm.setPrice(Integer.parseInt(value));
                        break;
                    default:
                        System.out.println("not matching keyword");
                        break;
                }
            }

            File resized = resize(productImgfile);
            FileInputStream fileInputStream = new FileInputStream(resized);
            MockMultipartFile mockMultipartFile = new MockMultipartFile(resized.getName(), resized.getName(), null, fileInputStream);

            retProducts.add(productService.upload(mockMultipartFile, owner, productUploadForm));
        }
        return retProducts;
    }

    private File resize(File productImgfile) throws IOException {
        String extension = FilenameUtils.getExtension(productImgfile.getName());

        String strPaentDir = productImgfile.getParent();
        String strResizeDir = strPaentDir + "\\resized\\";
        //String strResizeDir = strPaentDir + "/resized/";
        String strDestFile = strResizeDir + productImgfile.getName();
        File destFile = new File(strDestFile);
        FileUtils.forceMkdirParent(destFile);

        BufferedImage read = ImageIO.read(productImgfile);

        Image scaledInstance = read.getScaledInstance(300, 300, Image.SCALE_DEFAULT);
        BufferedImage bufferedImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(scaledInstance, 0, 0, null);

        ImageIO.write(bufferedImage, extension, destFile);
        return destFile;
    }
}
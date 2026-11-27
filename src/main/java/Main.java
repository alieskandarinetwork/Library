import config.ConnectionProvider;
import lombok.extern.slf4j.Slf4j;
import model.dto.BookDto;
import model.dto.PersonDto;
import model.dto.create.CreateBookDto;
import model.dto.create.CreateBorrowDto;
import model.dto.create.CreatePersonDto;
import repository.impl.BookRepositoryImpl;
import repository.impl.BorrowRepositoryImpl;
import repository.impl.PersonRepositoryImpl;
import service.impl.BookServiceImpl;
import service.impl.BorrowServiceImpl;
import service.impl.PersonServiceImpl;
import controller.BookController;
import controller.PersonController;
import controller.BorrowController;

import mapper.BookMapper;
import mapper.PersonMapper;
import mapper.BorrowMapper;

@Slf4j
public class Main {
    public static void main(String[] args) {
        try {
            ConnectionProvider connectionProvider = new ConnectionProvider();

            BookRepositoryImpl bookRepository =
                    new BookRepositoryImpl(connectionProvider, null, null);

            PersonRepositoryImpl personRepository =
                    new PersonRepositoryImpl(connectionProvider, null, null);

            BorrowRepositoryImpl borrowRepository =
                    new BorrowRepositoryImpl(connectionProvider, null, null);

            BookServiceImpl bookService
                    = new BookServiceImpl(bookRepository, borrowRepository);

            PersonServiceImpl personService
                    = new PersonServiceImpl(personRepository, borrowRepository);

            BorrowServiceImpl borrowService
                    = new BorrowServiceImpl(borrowRepository, bookRepository, personRepository);


            BookController bookController =
                    new BookController(bookService, new BookMapper());

            PersonController personController =
                    new PersonController(personService, new PersonMapper());

            BorrowController borrowController =
                    new BorrowController(borrowService, new BorrowMapper());

            PersonDto personDto = personController.create
                    (new CreatePersonDto(
                            "Reza",
                            "Rezaie",
                            "1888665"
                    ));
            System.out.println(" شخص با موفقیت ثبت شد" + personDto);

            BookDto bookDto = bookController.create(
                    new CreateBookDto(
                            "Spring",
                            "Boot",
                            "300",
                            2015
                    ));
            System.out.println("کتاب با موفقیت ثبت شد: " + bookDto);

            System.out.println("در حال ثبت امانت : ");

            System.out.println(borrowController.create(
                    new CreateBorrowDto(bookDto.id(), personDto.id())
            ));

            log.info("========== لیست کتاب‌ها ==========");
            bookController.findAll()
                    .forEach(book -> log.info("{}", book));

            log.info("========== لیست کتاب‌ها پس از بازگشت ==========");
            bookController.findAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("خطا در اجرای برنامه : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

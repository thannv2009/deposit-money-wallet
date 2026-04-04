package vn.deposit.core;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.deposit.core.config.DepositApiConfiguration;

@TestConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WalletServiceImplTest.class})
public class WalletServiceImplTest {

}

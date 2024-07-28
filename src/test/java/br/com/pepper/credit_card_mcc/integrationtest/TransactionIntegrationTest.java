package br.com.pepper.credit_card_mcc.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import jakarta.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.model.mechant.Merchant;
import br.com.pepper.credit_card_mcc.model.transaction.Transaction;
import br.com.pepper.credit_card_mcc.repository.account.AccountRepository;
import br.com.pepper.credit_card_mcc.repository.merchant.MerchantRepository;
import br.com.pepper.credit_card_mcc.repository.transaction.TransactionRepository;
import br.com.pepper.credit_card_mcc.utils.AbstractIntegrationTests;

class TransactionIntegrationTest extends AbstractIntegrationTests {

    @Resource
    private MockMvc mockMvc;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private TransactionRepository transactionRepository;

    @Resource
    private MerchantRepository merchantRepository;

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE })
    void transaction_with_mccs_success(String mcc) throws Exception {
        MccType mccType = MccType.fromMcc(mcc);
        Account account = new Account(1L, new BigDecimal("100.00"), new BigDecimal("100.00"), new BigDecimal("100.00"));
        accountRepository.save(account);

        String transactionJson = "{"
                + "\"account\": \"1\","
                + "\"totalAmount\": 50.00,"
                + "\"mcc\": \""+mcc+"\","
                + "\"merchant\": \"John Doo\""
                + "}";

        mockMvc.perform(post("/transactions/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("00"));

        Transaction transaction = transactionRepository.findAll().get(0);
        assertEquals(mccType, transaction.getMcc());
        assertEquals("John Doo", transaction.getMerchantName());
        assertEquals(new BigDecimal("50.00"), transaction.getAmount());
        assertEquals(account.getId(), transaction.getAccount().getId());
        assertEquals(TransactionStatusType.APPROVED, transaction.getStatus());
    }

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE })
    void transaction_with_mccs_decline_and_use_cash_balance_if_widhout_balance(String mcc) throws Exception {
        Account account = new Account(1L, new BigDecimal("1.00"), new BigDecimal("1.00"), new BigDecimal("1.00"));
        accountRepository.save(account);

        String transactionJson = "{"
                + "\"account\": \"1\","
                + "\"totalAmount\": 50.00,"
                + "\"mcc\": \""+mcc+"\","
                + "\"merchant\": \"John Doo\""
                + "}";

        mockMvc.perform(post("/transactions/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("51"));

        Transaction transaction = transactionRepository.findAll().get(0);
        assertEquals(MccType.CASH, transaction.getMcc());
        assertEquals("John Doo", transaction.getMerchantName());
        assertEquals(new BigDecimal("50.00"), transaction.getAmount());
        assertEquals(account.getId(), transaction.getAccount().getId());
        assertEquals(TransactionStatusType.DECLINED, transaction.getStatus());
    }

    @Test
    void transaction_with_mcc_found_by_merchant() throws Exception {
        Account account = new Account(1L, new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("100.00"));
        accountRepository.save(account);

        String merchantName = "UBER EATS SAO PAULO BR";
        Merchant merchant = new Merchant(1L, merchantName, MccType.CASH);
        merchantRepository.save(merchant);

        String transactionJson = "{"
                + "\"account\": \"1\","
                + "\"totalAmount\": 50.00,"
                + "\"mcc\": \"9999\","
                + "\"merchant\": \""+merchantName+"\""
                + "}";

        mockMvc.perform(post("/transactions/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("00"));

        Transaction transaction = transactionRepository.findAll().get(0);
        assertEquals(MccType.CASH, transaction.getMcc());
        assertEquals(merchantName, transaction.getMerchantName());
        assertEquals(new BigDecimal("50.00"), transaction.getAmount());
        assertEquals(account.getId(), transaction.getAccount().getId());
        assertEquals(TransactionStatusType.APPROVED, transaction.getStatus());
    }

    @Test
    void trasaction_with_internal_exception() throws Exception {

        String transactionJson = "{"
                + "\"account\": \"1\","
                + "\"totalAmount\": 50.00,"
                + "\"mcc\": \"9999\","
                + "\"merchant\": \"John Doo\""
                + "}";

        mockMvc.perform(post("/transactions/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("07"));
    }

    @Test
    void trasaction_with_bad_request_exception() throws Exception {

        String transactionJson = "{"
                + "\"account\": \"1\","
                + "\"mcc\": \"9999\","
                + "}";

        mockMvc.perform(post("/transactions/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("07"));
    }
}

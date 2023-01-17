package mapoints.mpesa.repository;

import mapoints.lib.repository.BaseRepository;
import mapoints.mpesa.model.MpesaTrxValidationInitResponse;
import java.util.Optional;

public interface MPesaTrxInitValidationRepository extends BaseRepository<MpesaTrxValidationInitResponse> {
    Optional<MpesaTrxValidationInitResponse> findByConversationId(String conversationId);
}

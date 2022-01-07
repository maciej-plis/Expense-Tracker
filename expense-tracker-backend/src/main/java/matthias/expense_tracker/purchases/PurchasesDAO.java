package matthias.expense_tracker.purchases;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface PurchasesDAO extends JpaRepository<PurchaseGroupEntity, UUID> {
}
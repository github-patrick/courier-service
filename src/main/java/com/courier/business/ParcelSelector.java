package com.courier.business;

import com.courier.domain.Parcel;
import com.courier.domain.enums.Priority;
import com.courier.repository.ParcelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
@Service
public class ParcelSelector implements ParcelSelectable {

    private ParcelRepository parcelRepository;

    private ArrayBlockingQueue<Parcel> highPriorityQueue = new ArrayBlockingQueue(100);
    private ArrayBlockingQueue<Parcel> mediumPriorityQueue = new ArrayBlockingQueue(100);
    private ArrayBlockingQueue<Parcel> lowPriorityQueue = new ArrayBlockingQueue(100);

    @Autowired
    public ParcelSelector(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    @Override
    public Optional<Parcel> selectParcelHighPriority() {
        parcelRepository.findParcelByPriority(Priority.HIGH).forEach(
                parcel -> {
                    if (parcel.getDeliverer() == null) {
                        if (!highPriorityQueue.contains(parcel)) {
                            highPriorityQueue.add(parcel);
                        }
                    }
                }
        );
        if (highPriorityQueue.size() != 0) {
            log.info("Parcel {} is waiting in queue to be assigned to a driver.", highPriorityQueue.element().getId());
            return Optional.of(highPriorityQueue.element());
        } else {
            log.info("No high priority parcel to be sent.");
            return Optional.empty();
        }
    }

    @Override
    public Optional<Parcel> selectParcelMediumPriority() {
        parcelRepository.findParcelByPriority(Priority.MEDIUM).forEach(
                parcel -> {
                    if (parcel.getDeliverer() == null) {
                        mediumPriorityQueue.add(parcel);
                    }
                }
        );
        if (mediumPriorityQueue.size() != 0) {
            return Optional.of(mediumPriorityQueue.element());
        } else {
            log.info("No medium priority parcel to be sent.");
            return Optional.empty();
        }
    }

    @Override
    public Optional<Parcel> selectParcelLowPriority() {
        parcelRepository.findParcelByPriority(Priority.LOW).forEach(
                parcel -> {
                    if (parcel.getDeliverer() == null) {
                        lowPriorityQueue.add(parcel);
                    }
                }
        );
        if (lowPriorityQueue.size() != 0) {
            return Optional.of(lowPriorityQueue.element());
        } else {
            log.info("No low priority parcel to be sent.");
            return Optional.empty();
        }
    }

    @Override
    public boolean removeFromHighPriorityQueue(Parcel parcel) {
        log.info("Removing parcel id: {} from the high priority queue.", parcel.getId());
        log.info("High priority queue size: {}", highPriorityQueue.size());
        return highPriorityQueue.remove(parcel);
    }

    @Override
    public boolean removeFromMediumPriorityQueue(Parcel parcel) {
        log.info("Removing parcel id: {} from the medium priority queue.", parcel.getId());
        log.info("Medium priority queue size: {}", mediumPriorityQueue.size());
        return mediumPriorityQueue.remove(parcel);
    }

    @Override
    public boolean removeFromLowPriorityQueue(Parcel parcel) {
        log.info("Removing parcel id: {} from the low priority queue.", parcel.getId());
        log.info("Low priority queue size: {}", mediumPriorityQueue.size());
        return lowPriorityQueue.remove(parcel);
    }
}

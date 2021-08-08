package com.saleseaze.api.repository

import com.saleseaze.api.entity.PublishPost
import org.springframework.data.repository.CrudRepository

interface PublishPostRepository: CrudRepository<PublishPost, String> {
}
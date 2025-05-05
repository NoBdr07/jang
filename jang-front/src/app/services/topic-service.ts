import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { TopicDTO } from "../models/topic.model";

@Injectable({
    providedIn: 'root'
})
export class TopicService {
    private readonly url = `${environment.apiUrl}/topics`;
    
    constructor(private http: HttpClient) {}

    getTopics(): Observable<TopicDTO[]> {
        return this.http.get<TopicDTO[]>(this.url);
    }
}